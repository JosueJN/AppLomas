package com.dasc.applomas

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val spVendedor =  getSharedPreferences("CelularActivado", Context.MODE_PRIVATE)
        val pwd = spVendedor.getString("pwdAnciano", "")


        btnAcceder.setOnClickListener {
            var pwdApp = etPassword.text.toString().trim()

            if (pwdApp.isEmpty()) {
                Toast.makeText(this, "Introudce una contraseña", Toast.LENGTH_LONG).show()
            }

            if(pwd != pwdApp) {

                Log.i("nava", "P "+pwd+" - "+pwdApp)
                Toast.makeText(this,"Contraseña Incorrecta", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}