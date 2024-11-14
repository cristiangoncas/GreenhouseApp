package com.cristiangoncas.greenhousemonitor.domain.data.local

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import com.cristiangoncas.greenhousemonitor.domain.entity.RawLogEntry

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