package com.ikresimir.weatherilicic.source.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ikresimir.weatherilicic.model.UserData

@Database(
    entities = [UserData::class],
    version = 2,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ],
    exportSchema = true
)

abstract class LocalDatabase: RoomDatabase() {
    abstract val localDao: LocalDao
    companion object{
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase{
            synchronized(this){
                return INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "mbanking"
                ).allowMainThreadQueries().build().also{
                    INSTANCE = it
                }
            }
        }
    }
}