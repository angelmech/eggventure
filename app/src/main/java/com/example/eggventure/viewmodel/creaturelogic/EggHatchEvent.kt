package com.example.eggventure.viewmodel.creaturelogic

import android.util.Log
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
        val hatchedCreature = pickCreatureBasedOnRarity(creatureData)

        // Create a CreatureEntity with timestamp
        val creatureEntity = CreatureEntity(
            creatureName = hatchedCreature.name,
            rarity = hatchedCreature.rarity,
            imageResId = hatchedCreature.imageResId,
            hatchedAt = hatchTimestamp
        )

        // Save the new creature to DB
        creatureRepository.insertCreature(creatureEntity)
        Log.d("EggHatchEvent", "Hatched creature: ${creatureEntity.creatureName} at ${creatureEntity.hatchedAt}")

        return@withContext creatureEntity
    }

    /**
     * Pick a creature weighted by rarity:
     * TODO: Later enhance with sensor data or user context influencing chances.
     */
    private fun pickCreatureBasedOnRarity(
        creatureData: CreatureDataInterface
    ): Creature {
        // Define rarity chances (sum to 100)
        val rarityRoll = Random.nextInt(100)
        val rarity = when (rarityRoll) {
            in 0..39 -> Rarity.COMMON         // 40%
            in 40..69 -> Rarity.RARE        // 30%
            in 70..89 -> Rarity.EPIC        // 20%
            in 90..97 -> Rarity.LEGENDARY   // 8%
            else -> Rarity.MYTHICAL               // 2%
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

        Log.d("EggHatchEvent", "Picked ${rarity.name} creature: ${possibleCreatures.first().name}")

        // Pick random creature from filtered list
        return possibleCreatures.random()
    }

}
