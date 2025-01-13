package com.cristiangoncas.greenhousemonitor.framework.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.cristiangoncas.greenhousemonitor.framework.local.model.DbLogEntry

@Database(entities = [DbLogEntry::class], version = 1, exportSchema = false)
abstract class GreenhouseDB : RoomDatabase() {

    abstract fun logEntryDao(): LogEntryDao

    companion object {
        private const val DATABASE_NAME = "greenhouse.db"

        @Volatile
        var INSTANCE: GreenhouseDB? = null

        fun getInstance(context: Context): GreenhouseDB {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context,
                    GreenhouseDB::class.java,
                    DATABASE_NAME,
                )
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}
