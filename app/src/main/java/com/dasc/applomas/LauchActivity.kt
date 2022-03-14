package com.dasc.applomas

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.dasc.applomas.DataBase.AppDatabase
import com.dasc.applomas.Modelos.AsuntosTBL
import com.dasc.applomas.Retrofit.Api
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LauchActivity : AppCompatActivity() {

    private val database: AppDatabase by lazy { AppDatabase.invoke(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lauch)

        val activado =  getSharedPreferences("CelularActivado", Context.MODE_PRIVATE)
        val status = activado.getString("activado", "0")

        if (status == "0") {
            Handler().postDelayed({
                startActivity(Intent(this, ActivacionActivity::class.java))
            }, 1000)
        } else {
            if (isNetworkAvailable()) {
                actualizaInfor()
            }
            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
            }, 1000)
        }
    }

    private fun actualizaInfor() {

        Log.i("nava", "Actualizar")
        CoroutineScope(Dispatchers.IO).launch {
            doAsync {
                database.asuntosTBLDao().borraallAsunto()
            }

        }

        downInfo()

    }

    fun getRetrofit(): Retrofit {
        return  Retrofit.Builder()
            .baseUrl("https://www.lomasebano.com/ApiLomas/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun downInfo() {
        Log.i("nava","Down0")
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(Api::class.java).FolioUltimo("ApiAsuntos.php", "5")
            runOnUiThread {
                if (call.isSuccessful) {
                    var data = call.body()?.Asuntos!!
                    val jsonData = Gson().toJson(data)
                    Log.i("nava","Data "+jsonData)
                    //Log.i("nava","Down1 "+t)
                    doAsync {
                        for (i in data) {
                            database.asuntosTBLDao().agregaAsuntos(i)
                            Log.i("nava", "AsuntodoAsync "+i.publicador)
                        }
                    }
                } else {
                    Toast.makeText(applicationContext,"No hay Información para actualizar", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
}