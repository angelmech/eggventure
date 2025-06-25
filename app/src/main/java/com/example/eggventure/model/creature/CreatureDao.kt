package com.example.eggventure.model.creature

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CreatureDao {
    @Insert
    suspend fun insertCreature(creature: CreatureEntity)

    @Query("SELECT * FROM creatures ORDER BY hatchedAt DESC")
    fun getAllCreatures(): Flow<List<CreatureEntity>>
}
