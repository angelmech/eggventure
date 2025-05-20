package com.example.eggventure.model.repository

import android.util.Log
import com.example.eggventure.model.dao.HatchProgressDao
import com.example.eggventure.model.entity.HatchProgressEntity

class HatchProgressRepository(private val hatchProgressDao: HatchProgressDao) {

    suspend fun insertProgress(progress: HatchProgressEntity) {
        hatchProgressDao.insertProgress(progress)
        Log.d("HatchProgressRepo", "Hatch progress inserted successfully: $progress")
    }

    suspend fun getHatchProgressSteps(): Int? {
        val steps = hatchProgressDao.getHatchProgressSteps()
        Log.d("HatchProgressRepo", "Steps accumulated retrieved: $steps")
        return steps
    }

    suspend fun getHatchGoal(): Int? {
        val hatchGoal = hatchProgressDao.getHatchGoal()
        Log.d("HatchProgressRepo", "Hatch goal retrieved: $hatchGoal")
        return hatchGoal
    }

    suspend fun getLastHatchProgress(): HatchProgressEntity? {
        val lastProgress = hatchProgressDao.getLastHatchProgress()
        Log.d("HatchProgressRepo", "Last hatch progress retrieved: $lastProgress")
        return lastProgress
    }

    suspend fun updateHatchProgress(id: Int, stepsAccumulated: Int) {
        hatchProgressDao.updateHatchProgress(id, stepsAccumulated)
        Log.d("HatchProgressRepo", "Hatch progress updated successfully. ID: $id, Steps: $stepsAccumulated")
    }
}