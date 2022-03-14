package com.dasc.applomas.DataBase

import android.content.Context
import android.os.Build
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dasc.applomas.Modelos.AsuntosTBL

@Database(entities = [AsuntosTBL::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun asuntosTBLDao(): AsuntosTBLDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "cobPdb"
        ).build()
        /*
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appLomas"
                ).build()

                INSTANCE = instance

                return instance
            }
        }

         */


    }

}