package com.example.eggventure.model.creature

import kotlinx.coroutines.flow.Flow

class CreatureRepositoryImpl(private val creatureDao: CreatureDao) : CreatureRepository {

    override suspend fun insertCreature(creatures: CreatureEntity) {
        creatureDao.insertCreature(creatures)
    }

    override fun getAllCreatures(): Flow<List<CreatureEntity>> {
        return creatureDao.getAllCreatures()
    }
}