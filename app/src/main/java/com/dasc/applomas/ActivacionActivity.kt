package com.dasc.applomas

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dasc.applomas.DataBase.AppDatabase
import com.dasc.applomas.Retrofit.Api
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_activacion.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActivacionActivity : AppCompatActivity() {

    private val database: AppDatabase by lazy { AppDatabase.invoke(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activacion)

        botonActivar.setOnClickListener {
            val nombre = tvNombre.text.toString().trim()
            val pwd = passwdVendedor.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this,"Ingresa tu nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if(pwd.isEmpty()) {
                Toast.makeText(this, "Ingresa una contraseña", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            val activado = getSharedPreferences("CelularActivado", Context.MODE_PRIVATE)
            val editActivado = activado.edit()
            editActivado.putString("nombreAnciano", nombre)
            editActivado.putString("pwdAnciano", pwd)
            editActivado.putString("activado","1")
            editActivado.apply()

            if (isNetworkAvailable()) {
                actualizaInfor()
            }

            startActivity(Intent(this, MainActivity::class.java))
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