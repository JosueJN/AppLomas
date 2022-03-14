package com.dasc.applomas.Adaptador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dasc.applomas.Modelos.AsuntosTBL
import com.dasc.applomas.R
import kotlinx.android.synthetic.main.lista_asuntos.view.*

class AdaptadorAsuntos(private val mContext: Context, private val listaAsuntos: List<AsuntosTBL>): ArrayAdapter<AsuntosTBL>(mContext, 0, listaAsuntos) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layaout  = LayoutInflater.from(mContext).inflate(R.layout.lista_asuntos, parent, false)

        val asunto = listaAsuntos[position]

        layaout.tvPublicador.text = asunto.publicador
        layaout.tvfecha.text = asunto.fecha

        return layaout
    }
}