package com.dasc.applomas.Modelos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PendientesRnTBL (
    @PrimaryKey(autoGenerate = true)
    val idPendiente: Int,
    val Pendiente: String,
    val fecha: String,
    val pendienteTxt: String
)