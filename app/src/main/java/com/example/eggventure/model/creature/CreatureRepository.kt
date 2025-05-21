package com.example.eggventure.model.creature

class CreatureRepository(private val creatureDao: CreatureDao) {

    suspend fun insertCreature(creatures: CreatureEntity) {
        creatureDao.insertCreature(creatures)
    }

    suspend fun getCreatureName(creatureId: Int): String? {
        return creatureDao.getCreatureName(creatureId)
    }

    suspend fun getCreatureRarity(creatureId: Int): String? {
        return creatureDao.getCreatureRarity(creatureId)
    }

    suspend fun getCreatureHatchTime(creatureId: Int): Long? {
        return creatureDao.getCreatureHatchTime(creatureId)
    }

    suspend fun getAllCreatures(): List<CreatureEntity> {
        return creatureDao.getAllCreatures()
    }
}