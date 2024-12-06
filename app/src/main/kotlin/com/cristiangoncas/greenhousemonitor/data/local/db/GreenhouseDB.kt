package com.cristiangoncas.greenhousemonitor.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.cristiangoncas.greenhousemonitor.data.local.model.LogEntry
import com.cristiangoncas.greenhousemonitor.data.local.model.RawLogEntry

@Database(entities = [RawLogEntry::class, LogEntry::class], version = 1, exportSchema = false)
abstract class GreenhouseDB : RoomDatabase() {

    abstract fun rawLogEntryDao(): RawLogDao

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
