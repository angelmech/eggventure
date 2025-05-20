package com.example.eggventure.model.repository

import com.example.eggventure.model.dao.EggDao
import com.example.eggventure.model.entity.EggEntity
import kotlinx.coroutines.flow.Flow

class EggRepository(private val eggDao: EggDao) {

    suspend fun insertEgg(egg: EggEntity) {
        eggDao.insertEgg(egg)
    }

    suspend fun getEggName(eggId: Int): String? {
        return eggDao.getEggName(eggId)
    }

    suspend fun getEggRarity(eggId: Int): String? {
        return eggDao.getEggRarity(eggId)
    }

    suspend fun getEggHatchTime(eggId: Int): Long? {
        return eggDao.getEggHatchTime(eggId)
    }

    suspend fun getAllEggs(): List<EggEntity> {
        return eggDao.getAllEggs()
    }
}