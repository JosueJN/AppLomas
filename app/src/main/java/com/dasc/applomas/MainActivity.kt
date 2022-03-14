package com.dasc.applomas

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dasc.applomas.Asuntos.AsuntosActivity
import com.dasc.applomas.Pendientes.PendientesActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val activado = getSharedPreferences("CelularActivado", Context.MODE_PRIVATE)
        val nombreAnciano = activado.getString("nombreAnciano", "")

        tvBienvenido.text = "Bienvenido "+nombreAnciano

        asuntosAtendidos.setOnClickListener {
            startActivity(Intent(this, AsuntosActivity::class.java))
        }

        botonPendientes.setOnClickListener {
            startActivity(Intent(this, PendientesActivity::class.java))
        }
    }
}