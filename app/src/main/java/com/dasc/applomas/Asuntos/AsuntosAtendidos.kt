package com.dasc.applomas.Asuntos

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dasc.applomas.DataBase.AppDatabase
import com.dasc.applomas.Modelos.AsuntosTBL
import com.dasc.applomas.R
import com.dasc.applomas.Retrofit.Api
import kotlinx.android.synthetic.main.activity_asuntos_atendidos.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AsuntosAtendidos : AppCompatActivity() {

    var idAsunto: String = "0"
    var asuntoAtendido: String? = "0"
    var publicadorTxt: String = ""
    var fechaTxt: String = ""
    var atendieronTxt: String = ""
    var descripcionTxt: String = ""
    var status: String = "0"

    private val database: AppDatabase by lazy { AppDatabase.invoke(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asuntos_atendidos)


        var spAsunto: SharedPreferences = getSharedPreferences("asuntoAtentido", Context.MODE_PRIVATE)
        asuntoAtendido = spAsunto.getString("asuntoId", "0")

        Log.i("nava", "Valor: "+asuntoAtendido)

        if (asuntoAtendido!!.toInt() != 0 ) {

            doAsync {
                val asunto = database.asuntosTBLDao().muestraAsunto(asuntoAtendido!!.toInt())

                etPublicador.setText(asunto.get(0).publicador)
                etDate.setText(asunto.get(0).fecha)
                etAtendieron.setText(asunto.get(0).atendieron)
                etDescripcion.setText(asunto.get(0).asunto)
                idAsunto = asunto.get(0).idAsunto
            }
        }

        botonGuardarAsunto.setOnClickListener {

            if (!isNetworkAvailable()) {
                var alertaActualizar = AlertDialog.Builder(this)

                alertaActualizar.setTitle("NOTA:")
                alertaActualizar.setMessage("Active sus datos ó el Wifi para tener acceso a este modulo")
                alertaActualizar.setPositiveButton("PRECIONE") { dialogInterface: DialogInterface, i: Int ->
                    startActivity(Intent(this, AsuntosActivity::class.java))
                }
                alertaActualizar.show()
            } else {

                publicadorTxt = etPublicador.text.toString()
                fechaTxt = etDate.text.toString()
                atendieronTxt = etAtendieron.text.toString()
                descripcionTxt = etDescripcion.text.toString()

                Log.i("nava", "Tipo " + idAsunto)

                if (asuntoAtendido == "0") {
                    CoroutineScope(Dispatchers.IO).launch {
                        ultimoAsunto()
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        var tipo = "Update"
                        val asunto = AsuntosTBL(
                            asuntoAtendido!!,
                            publicadorTxt,
                            fechaTxt,
                            atendieronTxt,
                            descripcionTxt,
                            status
                        )
                        doAsync {
                            database.asuntosTBLDao().actualizaAsunto(asunto)
                        }
                        Log.i("nava", "Actualizar")
                        upNube(
                            asuntoAtendido!!,
                            publicadorTxt!!,
                            fechaTxt!!,
                            atendieronTxt!!,
                            descripcionTxt!!,
                            status!!,
                            tipo
                        )
                        this@AsuntosAtendidos.finish()
                    }

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.asuntos_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.delete_item -> {
                //productoLiveData.removeObservers(this)
                Log.i("nava", "BorradoID "+asuntoAtendido)

                CoroutineScope(Dispatchers.IO).launch {
                    database.asuntosTBLDao().borrarAsunto(asuntoAtendido!!.toInt())
                    this@AsuntosAtendidos.finish()
                }
            }
        }

        var tipo = "delete"
        upNube(asuntoAtendido!!, publicadorTxt!!, fechaTxt!!, atendieronTxt!!, descripcionTxt!!, status!!, tipo)
        return super.onOptionsItemSelected(item)
    }

    private fun upNube(id: String, publicador: String, fecha: String, atendieron: String, descripcion: String, status: String, tipo: String) {

        Log.i("nava", "noooo "+id+" pub "+publicador)
        if (isNetworkAvailable() && id != "0") {
            Log.i("nava", "aqui toy ")
            val url = "https://www.lomasebano.com/ApiLomas/ApiAsunto.php"
            val queue = Volley.newRequestQueue(this)
            var resultadoPost = object : StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    Toast.makeText(this, "Se actualizo la información", Toast.LENGTH_SHORT).show()
                }, Response.ErrorListener { error ->
                    Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show()
                }) {
                override fun getParams(): MutableMap<String, String> {
                    val paramentros = HashMap<String, String>()
                    paramentros.put("id", id)
                    paramentros.put("publicador", publicador)
                    paramentros.put("fecha", fecha)
                    paramentros.put("atendieron", atendieron)
                    paramentros.put("descripcion", descripcion)
                    paramentros.put("status", status)
                    paramentros.put("tipo", tipo)
                    return paramentros
                }
            }
            queue.add(resultadoPost)
        }

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
            .baseUrl("https://www.lomasebano.com/ApiLomas/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun ultimoAsunto() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(Api::class.java).FolioUltimo("ApiUltAsunto.php", "5")
            runOnUiThread {
                if (call.isSuccessful) {
                    asuntoAtendido = call.body()!!.FOLIO.get(0).idAsunto
                    var asuntoNuevo = asuntoAtendido!!.toInt()+1
                    Log.i("nava","nuevoooo "+asuntoNuevo)
                    val asunto = AsuntosTBL(asuntoNuevo.toString(), publicadorTxt, fechaTxt, atendieronTxt, descripcionTxt,status)
                    CoroutineScope(Dispatchers.IO).launch {
                        database.asuntosTBLDao().agregaAsunto(asunto)
                    }
                    var tipo = "Insert"
                    upNube(asuntoNuevo.toString(), publicadorTxt!!, fechaTxt!!, atendieronTxt!!, descripcionTxt!!, status!!, tipo)
                    this@AsuntosAtendidos.finish()
                } else {
                    Toast.makeText(applicationContext,"No hay Información para actualizar", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}