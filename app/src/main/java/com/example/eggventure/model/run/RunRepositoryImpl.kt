package com.example.eggventure.model.run

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RunRepositoryImpl(private val runDao: RunDao) : RunRepository {

    override suspend fun insertRun(run: RunEntity) {
        runDao.insertRun(run)
        Log.d("RunRepository", "Run inserted successfully: $run")
    }

    override fun getAllRuns(): Flow<List<RunEntity>> = flow {
        val runs = runDao.getAllRuns()
        emit(runs)
        Log.d("RunRepository", "All runs retrieved: $runs")
    }

    override suspend fun getLastRun(): RunEntity? {
        val lastRun = runDao.getLastRun()
        Log.d("RunRepository", "Last run retrieved: $lastRun")
        return lastRun
    }

    override suspend fun getLastRunDate(): Long? {
        val lastRunDate = runDao.getLastRunDate()
        Log.d("RunRepository", "Last run date retrieved: $lastRunDate")
        return lastRunDate
    }

    override suspend fun getLastRunDuration(): Long? {
        val lastRunDuration = runDao.getLastRunDuration()
        Log.d("RunRepository", "Last run duration retrieved: $lastRunDuration")
        return lastRunDuration
    }

    override suspend fun getLastRunDistance(): Float? {
        val lastRunDistance = runDao.getLastRunDistance()
        Log.d("RunRepository", "Last run distance retrieved: $lastRunDistance")
        return lastRunDistance
    }

    override suspend fun getLastRunAverageSpeed(): Float? {
        val lastRunAverageSpeed = runDao.getLastRunAverageSpeed()
        Log.d("RunRepository", "Last run average speed retrieved: $lastRunAverageSpeed")
        return lastRunAverageSpeed
    }

    override suspend fun getLastRunSteps(): Int? {
        val lastRunSteps = runDao.getLastRunSteps()
        Log.d("RunRepository", "Last run steps retrieved: $lastRunSteps")
        return lastRunSteps
    }

    override suspend fun getWeeklyAverage(weekAgo: Long): Double {
        val weeklyAverage = runDao.getWeeklyAverage(weekAgo)
        Log.d("RunRepository", "Weekly average retrieved: $weeklyAverage")
        return weeklyAverage
    }

    override suspend fun getWeeklyAverageDistance(weekAgo: Long): Double {
        val weeklyAverageDistance = runDao.getWeeklyAverageDistance(weekAgo)
        Log.d("RunRepository", "Weekly average distance retrieved: $weeklyAverageDistance")
        return weeklyAverageDistance
    }
}