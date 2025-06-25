package com.example.eggventure.viewmodel.creaturelogic

import android.util.Log
import com.example.eggventure.model.creature.Creature
import com.example.eggventure.model.creature.CreatureDataInterface
import com.example.eggventure.model.creature.CreatureEntity
import com.example.eggventure.model.creature.CreatureRepository
import com.example.eggventure.model.creature.CreatureType
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
        creatureData: CreatureDataInterface,
        lightLevel: Float?
    ): CreatureEntity? = withContext(Dispatchers.IO) {
        if (currentSteps < goal) {
            // Not yet hatched, just update progress
            hatchProgressRepository.updateHatchProgress(hatchId, currentSteps)
            return@withContext null
        }

        // Reset progress on hatch
        hatchProgressRepository.updateHatchProgress(hatchId, 0)

        val hatchedCreature = pickCreatureBasedOnRarityAndType(creatureData, lightLevel)

        val creatureEntity = CreatureEntity(
            creatureName = hatchedCreature.name,
            rarity = hatchedCreature.rarity,
            type = hatchedCreature.type,
            imageResId = hatchedCreature.imageResId,
            hatchedAt = hatchTimestamp
        )

        // Save the new creature to DB
        creatureRepository.insertCreature(creatureEntity)
        //Log.d("EggHatchEvent", "Hatched creature: ${creatureEntity.creatureName} at ${creatureEntity.hatchedAt}")

        return@withContext creatureEntity
    }


    /**
     * Selects a creature based on rarity chances.
     * also type is considered
     *
     * @return Creature
     */
    private fun pickCreatureBasedOnRarityAndType(
        creatureData: CreatureDataInterface,
        lightLevel: Float?
    ): Creature {
        val type = determineType(lightLevel)
        //Log.d("SensorLight", "Light level: $lightLevel lux")
        val rarity = rarityRoll()

        val possibleCreatures = creatureData.getAllCreatures()
            .filter { it.type == type }

        val chosen = possibleCreatures.randomOrNull()
            ?: creatureData.getAllCreatures().random() // fallback

        //Log.d("EggHatchEvent", "Picked $rarity $type creature: ${chosen.name}")

        return chosen.copy(rarity = rarity)
    }

    //----------Helper Functions----------

    /**
     * Determines the type of creature based on light level.
     *
     * @param light Light level in lux, can be null
     *
     * @return CreatureType based on the conditions
     */
    private fun determineType(light: Float?): CreatureType {
        if (light == null) return CreatureType.REGULAR
        return when {
            light < 700f -> CreatureType.SHADOW
            light < 10000f -> CreatureType.REGULAR
            else -> CreatureType.RADIANT
        }
    }

    /**
     * Defines the chances for each rarity type.
     */
    private val rarityChances = listOf(
        35 to Rarity.COMMON,      // 35%
        25 to Rarity.RARE,        // 25%
        20 to Rarity.EPIC,        // 20%
        15 to Rarity.LEGENDARY,   // 15%
        5 to Rarity.MYTHICAL     // 5%
    )

    /**
     * Rolls a rarity based on defined chances.
     * Returns a Rarity enum value.
     * @return Rarity
     */
    private fun rarityRoll(): Rarity {
        val roll = Random.nextInt(100)
        var cumulative = 0
        for ((chance, rarity) in rarityChances) {
            cumulative += chance
            if (roll < cumulative) return rarity
        }
        return Rarity.COMMON // Fallback
    }
}
