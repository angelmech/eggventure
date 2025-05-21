package com.example.eggventure.model.run
import kotlinx.coroutines.flow.Flow

interface RunRepository {
    suspend fun insertRun(run: RunEntity)
    fun getAllRuns(): Flow<List<RunEntity>>
    suspend fun getLastRun(): RunEntity?
    suspend fun getLastRunDate(): Long?
    suspend fun getLastRunDuration(): Long?
    suspend fun getLastRunDistance(): Float?
    suspend fun getLastRunAverageSpeed(): Float?
    suspend fun getLastRunSteps(): Int?
}
