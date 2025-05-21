package com.example.eggventure.model.repository

import android.util.Log
import com.example.eggventure.model.dao.RunDao
import com.example.eggventure.model.entity.RunEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RunRepository(private val runDao: RunDao) {

    suspend fun insertRun(run: RunEntity) {
        runDao.insertRun(run)
        Log.d("RunRepository", "Run inserted successfully: $run")
    }

    fun getAllRuns(): Flow<List<RunEntity>> = flow {
        val runs = runDao.getAllRuns()
        emit(runs)
        Log.d("RunRepository", "All runs retrieved: $runs")
    }

    suspend fun getLastRun(): RunEntity? {
        val lastRun = runDao.getLastRun()
        Log.d("RunRepository", "Last run retrieved: $lastRun")
        return lastRun
    }

    suspend fun getLastRunDate(): Long? {
        val lastRunDate = runDao.getLastRunDate()
        Log.d("RunRepository", "Last run date retrieved: $lastRunDate")
        return lastRunDate
    }

    suspend fun getLastRunDuration(): Long? {
        val lastRunDuration = runDao.getLastRunDuration()
        Log.d("RunRepository", "Last run duration retrieved: $lastRunDuration")
        return lastRunDuration
    }

    suspend fun getLastRunDistance(): Float? {
        val lastRunDistance = runDao.getLastRunDistance()
        Log.d("RunRepository", "Last run distance retrieved: $lastRunDistance")
        return lastRunDistance
    }

    suspend fun getLastRunAverageSpeed(): Float? {
        val lastRunAverageSpeed = runDao.getLastRunAverageSpeed()
        Log.d("RunRepository", "Last run average speed retrieved: $lastRunAverageSpeed")
        return lastRunAverageSpeed
    }

    suspend fun getLastRunSteps(): Int? {
        val lastRunSteps = runDao.getLastRunSteps()
        Log.d("RunRepository", "Last run steps retrieved: $lastRunSteps")
        return lastRunSteps
    }

    suspend fun getWeeklyAverage(weekAgo: Long): Double {
        val weeklyAverage = runDao.getWeeklyAverage(weekAgo)
        Log.d("RunRepository", "Weekly average retrieved: $weeklyAverage")
        return weeklyAverage
    }

    suspend fun getWeeklyAverageDistance(weekAgo: Long): Double {
        val weeklyAverageDistance = runDao.getWeeklyAverageDistance(weekAgo)
        Log.d("RunRepository", "Weekly average distance retrieved: $weeklyAverageDistance")
        return weeklyAverageDistance
    }
}