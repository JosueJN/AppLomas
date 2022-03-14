package com.dasc.applomas.Pendientes

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.dasc.applomas.Modelos.AsuntosTBL
import com.dasc.applomas.R
import com.dasc.applomas.Retrofit.Api
import kotlinx.android.synthetic.main.activity_pendientes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class PendientesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendientes)

        val file = File(getExternalFilesDir("https://www.lomasebano.com/ApiLomas/Cartas/"), "Anuncio.pdf")
        println(file)

 /*       pdfViewTest.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .enableDoubletap(true)
            .onDraw{canvas, pageWidth, pageHeight, displayedPage ->

            }.onDrawAll{canvas, pageWidth, pageHeight, displayedPage ->

            }
            .onPageChange { page, pageCount ->

            }.onPageError { page, t ->
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                Log.i("mf", ""+t.localizedMessage)
            }
            .onTap { false }
            .onRender {
                pdfViewTest.fitToWidth(0)
            }
            .enableAnnotationRendering(true)
            .load()
*/

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    fun getRetrofit(): Retrofit {
        return  Retrofit.Builder()
            .baseUrl("https://www.lomasebano.com/ApiLomas/Cartas/")
            .build()
    }

    private fun pendientes() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(Api::class.java).getAsuntos("Anuncio.pdf")
            runOnUiThread {
                if (call.isSuccessful) {

                } else {
                    Toast.makeText(applicationContext,"No hay Información para actualizar", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}