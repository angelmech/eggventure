package com.example.eggventure.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eggventure.model.dao.EggDao
import com.example.eggventure.model.dao.RunDao
import com.example.eggventure.model.entity.EggProgress
import com.example.eggventure.model.entity.RunEntity

@Database(
    entities = [RunEntity::class, EggProgress::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao
    abstract fun eggDao(): EggDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eggventure.db"
                ).build().also { INSTANCE = it }
            }
        }
    }

}
