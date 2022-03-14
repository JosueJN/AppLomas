package com.dasc.applomas.DataBase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dasc.applomas.Modelos.Asuntos
import com.dasc.applomas.Modelos.AsuntosTBL

@Dao
interface AsuntosTBLDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun agregaAsuntos(asuntosTBL: AsuntosTBL)

    @Query("SELECT * FROM asuntosTBL WHERE status = '0' ORDER BY fecha DESC")
    fun obtenAllRegistros(): LiveData<List<AsuntosTBL>>

    @Insert
    fun agregaAsunto(vararg asuntosTBL: AsuntosTBL)

    @Update
    fun actualizaAsunto(asuntosTBL: AsuntosTBL)

    @Query("DELETE FROM asuntosTBL")
    fun borraallAsunto(): Int

    @Query("UPDATE asuntosTBL SET status = '1' WHERE idAsunto = :asuntoId")
    fun borrarAsunto(asuntoId: Int)

    @Query("SELECT * FROM asuntosTBL WHERE idAsunto = :asuntoId AND status = '0'")
    fun muestraAsunto(asuntoId: Int) : List<AsuntosTBL>

    @Query("SELECT * FROM asuntosTBL ORDER BY idAsunto DESC LIMIT 1")
    fun ultimoAsunto() : List<AsuntosTBL>

}