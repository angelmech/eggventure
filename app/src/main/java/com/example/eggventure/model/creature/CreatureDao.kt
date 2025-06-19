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

    // get creature name
    @Query("SELECT creatureName FROM creatures WHERE id = :creatureId")
    suspend fun getCreatureName(creatureId: Int): String?

    // get creature rarity
    @Query("SELECT rarity FROM creatures WHERE id = :creatureId")
    suspend fun getCreatureRarity(creatureId: Int): String?

    // get creature hatch time
    @Query("SELECT hatchedAt FROM creatures WHERE id = :creatureId")
    suspend fun getCreatureHatchTime(creatureId: Int): Long?

}
