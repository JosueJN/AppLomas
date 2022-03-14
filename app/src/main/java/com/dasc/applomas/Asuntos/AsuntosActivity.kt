package com.dasc.applomas.Asuntos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.dasc.applomas.Adaptador.AdaptadorAsuntos
import com.dasc.applomas.DataBase.AppDatabase
import com.dasc.applomas.DataBase.AppDatabase_Impl
import com.dasc.applomas.Modelos.AsuntosTBL
import com.dasc.applomas.R
import com.dasc.applomas.Retrofit.Api
import kotlinx.android.synthetic.main.activity_asuntos.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AsuntosActivity : AppCompatActivity() {

    private val database: AppDatabase by lazy { AppDatabase.invoke(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asuntos)

        var listaAsuntos = emptyList<AsuntosTBL>()

        database.asuntosTBLDao().obtenAllRegistros().observe(this, Observer {
            listaAsuntos = it

            val adapter = AdaptadorAsuntos(this, listaAsuntos)

            lvlistaAsuntos.adapter = adapter
        })

        lvlistaAsuntos.setOnItemClickListener { parent, view, position, id ->

            var spAsunto: SharedPreferences = getSharedPreferences("asuntoAtentido", Context.MODE_PRIVATE)
            var editor = spAsunto.edit()
            editor.putString("asuntoId", listaAsuntos[position].idAsunto.toString())
            editor.apply()

            val intent = Intent(this, AsuntosAtendidos::class.java)
            intent.putExtra("asunto", listaAsuntos[position].idAsunto)
            Log.i("nava", "id "+listaAsuntos[position].idAsunto)
            startActivity(intent)
        }

        floatingActionButton.setOnClickListener {

            var spAsunto: SharedPreferences = getSharedPreferences("asuntoAtentido", Context.MODE_PRIVATE)
            var editor = spAsunto.edit()
            editor.putString("asuntoId", "0")
            editor.apply()
            startActivity(Intent(this, AsuntosAtendidos::class.java))
        }
    }
}