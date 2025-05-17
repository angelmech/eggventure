package com.example.eggventure.model.dao

import androidx.room.*
import com.example.eggventure.model.entity.RunEntity

@Dao
interface RunDao {
    @Insert
    suspend fun insertRun(run: RunEntity)

    @Query("SELECT * FROM runs ORDER BY date DESC")
    suspend fun getAllRuns(): List<RunEntity>

    @Query("SELECT AVG(steps) FROM runs WHERE date > :weekAgo")
    suspend fun getWeeklyStepsAverage(weekAgo: Long): Double

    @Query("SELECT SUM(steps) FROM runs")
    suspend fun getTotalSteps(): Int

}
