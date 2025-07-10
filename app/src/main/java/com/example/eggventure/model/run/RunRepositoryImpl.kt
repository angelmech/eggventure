package com.example.eggventure.model.run

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RunRepositoryImpl(private val runDao: RunDao) : RunRepository {

    override suspend fun insertRun(run: RunEntity) {
        runDao.insertRun(run)
        Log.d("RunRepository", "Run inserted successfully: $run")
    }

    override fun getAllRuns(): Flow<List<RunEntity>> {
        val runsFlow = runDao.getAllRuns()
        Log.d("RunRepository", "Subscribing to all runs flow.")
        return runsFlow
    }

    override suspend fun getLastRun(): RunEntity? {
        val lastRun = runDao.getLastRun()
        Log.d("RunRepository", "Last run retrieved: $lastRun")
        return lastRun
    }

    override fun getLast7Runs(): Flow<List<RunEntity>> {
        val last7Runs = runDao.getLast7Runs()
        Log.d("RunRepository", "Last 7 runs retrieved: $last7Runs")
        return last7Runs
    }
}