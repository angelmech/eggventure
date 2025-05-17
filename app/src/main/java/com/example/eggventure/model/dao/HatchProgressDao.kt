package com.example.eggventure.model.dao

import androidx.room.*
import com.example.eggventure.model.entity.HatchProgressEntity

@Dao
interface HatchProgressDao {
    @Insert
    suspend fun insertEggProgress(progress: HatchProgressEntity)

    @Query("SELECT SUM(stepsAccumulated) FROM hatch_progress")
    suspend fun getStepsForHatch(): Int
}
