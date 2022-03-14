package com.dasc.applomas.Modelos

import androidx.room.Entity

@Entity
class EstadisticasTBL (
    val idTrimestral: Int,
    val TotalPublicadores: String,
    val Publicadores: String,
    val HorasPub: String,
    val RevisitasPub: String,
    val EstudiosPub: String,
    val PreRegulares: String,
    val HorasReg: String,
    val RevisitasReg: String,
    val EstudiosReg: String,
    val PreAuxiliarees: String,
    val HorasAux: String,
    val RevisitasAux: String,
    val EstudiosAux: String,
    val Totaliregulares: String,
    val TotalInactivos: String
)