package com.dasc.applomas.Modelos

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "asuntosTBL")
data class AsuntosTBL (
    @PrimaryKey
    val idAsunto: String,
    val publicador: String,
    val fecha: String,
    val atendieron: String,
    val asunto: String,
    val status: String
) : Serializable