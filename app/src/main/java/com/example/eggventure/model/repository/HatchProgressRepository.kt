package com.example.eggventure.model.repository

import com.example.eggventure.model.dao.HatchProgressDao
import com.example.eggventure.model.entity.HatchProgressEntity

class HatchProgressRepository(private val eggDao: HatchProgressDao) {

    suspend fun insertEggProgress(progress: HatchProgressEntity) {
        eggDao.insertEggProgress(progress)
    }

    suspend fun getStepsForHatch(): Int {
        return eggDao.getStepsForHatch()
    }
}
