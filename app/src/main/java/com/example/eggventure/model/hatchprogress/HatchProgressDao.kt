package com.example.eggventure.model.hatchprogress

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HatchProgressDao {
    @Insert
    suspend fun insertProgress(progress: HatchProgressEntity)

    // get the last hatch progress
    @Query("SELECT * FROM hatch_progress ORDER BY id DESC LIMIT 1")
    suspend fun getLastHatchProgress(): HatchProgressEntity?

    // update the hatch progress
    @Query("UPDATE hatch_progress SET hatchProgressSteps = :stepsAccumulated WHERE id = :id")
    suspend fun updateHatchProgress(id: Int, stepsAccumulated: Int)
}
