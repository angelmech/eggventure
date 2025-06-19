package com.example.eggventure.model.hatchprogress

import android.util.Log

class HatchProgressRepositoryImpl(private val hatchProgressDao: HatchProgressDao) : HatchProgressRepository {

    override suspend fun insertProgress(progress: HatchProgressEntity) {
        hatchProgressDao.insertProgress(progress)
        Log.d("HatchProgressRepo", "Hatch progress inserted successfully: $progress")
    }

    override suspend fun getHatchProgressSteps(): Int? {
        val steps = hatchProgressDao.getHatchProgressSteps()
        Log.d("HatchProgressRepo", "Steps accumulated retrieved: $steps")
        return steps
    }

    override suspend fun getHatchGoal(): Int? {
        val hatchGoal = hatchProgressDao.getHatchGoal()
        Log.d("HatchProgressRepo", "Hatch goal retrieved: $hatchGoal")
        return hatchGoal
    }

    override suspend fun getLastHatchProgress(): HatchProgressEntity? {
        val lastProgress = hatchProgressDao.getLastHatchProgress()
        Log.d("HatchProgressRepo", "Last hatch progress retrieved: $lastProgress")
        return lastProgress
    }

    override suspend fun updateHatchProgress(id: Int, stepsAccumulated: Int) {
        hatchProgressDao.updateHatchProgress(id, stepsAccumulated)
        Log.d("HatchProgressRepo", "Hatch progress updated successfully. ID: $id, Steps: $stepsAccumulated")
    }
}