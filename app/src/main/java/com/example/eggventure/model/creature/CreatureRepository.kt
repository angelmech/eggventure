package com.example.eggventure.model.creature

interface CreatureRepository {
    suspend fun insertCreature(creatures: CreatureEntity)
    suspend fun getCreatureName(creatureId: Int): String?
    suspend fun getCreatureRarity(creatureId: Int): String?
    suspend fun getCreatureHatchTime(creatureId: Int): Long?
    suspend fun getAllCreatures(): List<CreatureEntity>
}