package com.example.eggventure.model.creature

import kotlinx.coroutines.flow.Flow

class CreatureRepositoryImpl(private val creatureDao: CreatureDao) : CreatureRepository {

    override suspend fun insertCreature(creatures: CreatureEntity) {
        creatureDao.insertCreature(creatures)
    }

    override suspend fun getCreatureName(creatureId: Int): String? {
        return creatureDao.getCreatureName(creatureId)
    }

    override suspend fun getCreatureRarity(creatureId: Int): String? {
        return creatureDao.getCreatureRarity(creatureId)
    }

    override suspend fun getCreatureHatchTime(creatureId: Int): Long? {
        return creatureDao.getCreatureHatchTime(creatureId)
    }

    override fun getAllCreatures(): Flow<List<CreatureEntity>> {
        return creatureDao.getAllCreatures()
    }
}