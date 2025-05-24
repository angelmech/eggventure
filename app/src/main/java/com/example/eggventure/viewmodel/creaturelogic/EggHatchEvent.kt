package com.example.eggventure.viewmodel.creaturelogic

import com.example.eggventure.model.creature.Creature
import com.example.eggventure.model.creature.CreatureDataInterface
import com.example.eggventure.model.creature.CreatureDatabase
import com.example.eggventure.model.creature.CreatureEntity
import com.example.eggventure.model.creature.CreatureRepository
import com.example.eggventure.model.creature.Rarity
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class EggHatchEvent(
    private val hatchProgressRepository: HatchProgressRepository,
    private val creatureRepository: CreatureRepository
) {

    /**
     * Process the hatch:
     * - Reset hatch progress if goal reached
     * - Select a creature based on rarity chances + timestamp
     * - Return the hatched CreatureEntity to caller for DB insertion or UI
     */
    suspend fun processHatchEvent(
        hatchId: Int,
        currentSteps: Int,
        goal: Int,
        hatchTimestamp: Long = System.currentTimeMillis(),
        creatureData: CreatureDataInterface
    ): CreatureEntity? = withContext(Dispatchers.IO) {
        if (currentSteps < goal) {
            // Not yet hatched, just update progress
            hatchProgressRepository.updateHatchProgress(hatchId, currentSteps)
            return@withContext null
        }

        // Reset progress on hatch
        hatchProgressRepository.updateHatchProgress(hatchId, 0)

        // Pick a creature based on rarity chances
        val hatchedCreature = pickCreatureBasedOnRarity(hatchTimestamp, creatureData)

        // Create a CreatureEntity with timestamp
        val creatureEntity = CreatureEntity(
            creatureName = hatchedCreature.name,
            rarity = hatchedCreature.rarity.name,
            hatchedAt = hatchTimestamp
        )

        // Save the new creature to DB
        creatureRepository.insertCreature(creatureEntity)

        return@withContext creatureEntity
    }

    /**
     * Pick a creature weighted by rarity:
     * TODO: Later enhance with sensor data or user context influencing chances.
     */
    private fun pickCreatureBasedOnRarity(
        timestamp: Long,
        creatureData: CreatureDataInterface
    ): Creature {
        // Define rarity chances (sum to 100)
        val rarityRoll = Random.nextInt(100)
        val rarity = when {
            rarityRoll < 40 -> Rarity.COMMON       // 40%
            rarityRoll < 70 -> Rarity.RARE         // 30%
            rarityRoll < 90 -> Rarity.EPIC         // 20%
            rarityRoll < 98 -> Rarity.LEGENDARY    // 8%
            else -> Rarity.MYTHICAL                 // 2%
        }

        // Get creatures of that rarity from passed creatureData
        val possibleCreatures = creatureData.getAllCreatures()
            .filter { it.rarity == rarity }

        // Defensive fallback if none found
        if (possibleCreatures.isEmpty()) {
            // fallback to common creatures from passed creatureData
            return creatureData.getAllCreatures()
                .first { it.rarity == Rarity.COMMON }
        }

        // Pick random creature from filtered list
        return possibleCreatures.random()
    }

}
