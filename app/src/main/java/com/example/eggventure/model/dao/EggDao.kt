package com.example.eggventure.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.eggventure.model.entity.EggEntity

@Dao
interface EggDao {
    @Insert
    suspend fun insertEgg(egg: EggEntity)

    @Query("SELECT * FROM eggs ORDER BY hatchedAt DESC")
    suspend fun getAllEggs(): List<EggEntity>

    // get egg name
    @Query("SELECT creatureName FROM eggs WHERE id = :eggId")
    suspend fun getEggName(eggId: Int): String?

    // get egg rarity
    @Query("SELECT rarity FROM eggs WHERE id = :eggId")
    suspend fun getEggRarity(eggId: Int): String?

    // get egg hatch time
    @Query("SELECT hatchedAt FROM eggs WHERE id = :eggId")
    suspend fun getEggHatchTime(eggId: Int): Long?

}
