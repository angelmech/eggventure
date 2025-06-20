package com.example.eggventure.model.run

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
    @Insert
    suspend fun insertRun(run: RunEntity)

    @Query("SELECT * FROM runs ORDER BY date DESC")
    fun getAllRuns(): Flow<List<RunEntity>>

    // get the last run
    @Query("SELECT * FROM runs ORDER BY date DESC LIMIT 1")
    suspend fun getLastRun(): RunEntity?

    // get the last run date
    @Query("SELECT date FROM runs ORDER BY date DESC LIMIT 1")
    suspend fun getLastRunDate(): Long?

    // get the last run duration
    @Query("SELECT duration FROM runs ORDER BY date DESC LIMIT 1")
    suspend fun getLastRunDuration(): Long?

    // get the last run distance
    @Query("SELECT distanceMeters FROM runs ORDER BY date DESC LIMIT 1")
    suspend fun getLastRunDistance(): Float?

    // get the last run average speed
    @Query("SELECT averageSpeed FROM runs ORDER BY date DESC LIMIT 1")
    suspend fun getLastRunAverageSpeed(): Float?

    // get the last run steps
    @Query("SELECT steps FROM runs ORDER BY date DESC LIMIT 1")
    suspend fun getLastRunSteps(): Int?

    // weekly average steps
    @Query("SELECT AVG(steps) FROM runs WHERE date > :weekAgo")
    suspend fun getWeeklyAverage(weekAgo: Long): Double

    // weekly average distance
    @Query("SELECT AVG(distanceMeters) FROM runs WHERE date > :weekAgo")
    suspend fun getWeeklyAverageDistance(weekAgo: Long): Double
}
