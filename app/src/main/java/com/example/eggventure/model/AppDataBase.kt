package com.example.eggventure.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eggventure.model.dao.EggDao
import com.example.eggventure.model.dao.HatchProgressDao
import com.example.eggventure.model.dao.RunDao
import com.example.eggventure.model.entity.EggEntity
import com.example.eggventure.model.entity.HatchProgressEntity
import com.example.eggventure.model.entity.RunEntity

@Database(
    entities = [RunEntity::class, HatchProgressEntity::class, EggEntity::class],
    version = 1,
    exportSchema = false // true if you want to export to JSON file
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eggDao(): EggDao
    abstract fun hatchProgressDao(): HatchProgressDao
    abstract fun runDao(): RunDao

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
