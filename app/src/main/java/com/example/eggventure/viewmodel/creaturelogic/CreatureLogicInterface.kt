package com.example.eggventure.viewmodel.creaturelogic

import android.hardware.lights.Light
import com.example.eggventure.model.creature.CreatureEntity
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for managing the logic related to creatures in the application.
 */
interface CreatureLogicInterface {
    /**
     * sorts list of owned creatures sorted by rarity.
     */
    fun toggleSortByRarity()

    /**
     * sorts list of owned creatures sorted by name.
     */
    fun toggleSortByName()

    /**
     * sorts list of owned creatures sorted by type.
     */
    fun toggleSortByType()

    /**
     * Resets the sorting of creatures to the default order.
     */
    fun resetSort()

    /**
     * Initiates the hatching process for a creature.
     *
     * @param creatureId The ID of the creature to hatch.
     * @param hatchProgressSteps The number of steps required to complete the hatching process.
     * @param hatchGoal The goal for the hatching process, typically the number of steps needed to hatch the creature.
     * @param totalSteps The total number of steps taken by the user, which may be used to track progress.
     * @param light The current light level, which may influence the hatching process or the type of creature hatched.
     *
     * @return True if the hatching process was successfully initiated, false otherwise.
     */
    suspend fun hatchCreature(creatureId: Int, totalSteps: Int, hatchProgressSteps: Int, hatchGoal: Int, light: Float?): Boolean
}