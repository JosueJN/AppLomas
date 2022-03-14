package com.dasc.applomas.Modelos

import androidx.room.Entity

@Entity
data class Asuntos (
    val idAsunto: String,
    val publicador: String,
    val fecha: String,
    val atendieron: String,
    val asunto: String,
    val status: String
)