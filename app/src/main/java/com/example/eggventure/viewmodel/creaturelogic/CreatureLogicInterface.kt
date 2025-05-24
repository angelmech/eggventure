package com.example.eggventure.viewmodel.creaturelogic

import com.example.eggventure.model.creature.CreatureEntity
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for managing the logic related to creatures in the application.
 */
interface CreatureLogicInterface {

    /**
     * Retrieves the list of owned creatures.
     * @return A StateFlow emitting the list of owned CreatureEntity objects.
     */
    val ownedCreatures: StateFlow<List<CreatureEntity>>

    /**
     * Adds a creature to the collection of owned creatures.
     * @param creature The CreatureEntity to add to the collection.
     */
    fun addCreatureToCollection(creature: CreatureEntity)

    /**
     * Retrieves a creature from the collection by its unique identifier.
     * @param creatureId The ID of the creature to retrieve.
     * @return The CreatureEntity if found, otherwise null.
     */
    fun getCreatureById(creatureId: Int): CreatureEntity?

    /**
     * Unlocks a creature.
     * @param creatureId The ID of the creature to unlock.
     */
    fun unlockCreature(creatureId: Int)

    /**
     * Checks if a creature is already owned by the user.
     * @param creatureId The ID of the creature to check.
     * @return True if the creature is owned, false otherwise.
     */
    fun hasCreature(creatureId: Int): Boolean

    /**
     * sorts list of owned creatures sorted by rarity.
     */
    fun toggleSortByRarity()

}