package com.example.eggventure.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eggventure.model.creature.CreatureDao
import com.example.eggventure.model.hatchprogress.HatchProgressDao
import com.example.eggventure.model.run.RunDao
import com.example.eggventure.model.creature.CreatureEntity
import com.example.eggventure.model.hatchprogress.HatchProgressEntity
import com.example.eggventure.model.run.RunEntity

@Database(
    entities = [RunEntity::class, HatchProgressEntity::class, CreatureEntity::class],
    version = 4,
    exportSchema = false // true if you want to export to JSON file
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun creatureDao(): CreatureDao
    abstract fun hatchProgressDao(): HatchProgressDao
    abstract fun runDao(): RunDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
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
