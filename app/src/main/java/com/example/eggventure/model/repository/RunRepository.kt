package com.example.eggventure.model.repository

import com.example.eggventure.model.dao.RunDao
import com.example.eggventure.model.entity.RunEntity

class RunRepository(private val runDao: RunDao) {

    suspend fun insertRun(run: RunEntity) {
        runDao.insertRun(run)
    }

    suspend fun getAllRuns(): List<RunEntity> {
        return runDao.getAllRuns()
    }

    suspend fun getWeeklyStepsAverage(weekAgo: Long): Double {
        return runDao.getWeeklyStepsAverage(weekAgo)
    }

    suspend fun getTotalSteps(): Int {
        return runDao.getTotalSteps()
    }
}
