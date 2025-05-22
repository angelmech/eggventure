package com.example.eggventure.model.creature

/**
 * Repository interface for managing creature data.
 */
interface CreatureRepository {
    /**
     * Inserts a new creature into the data source.
     *
     * @param creatures The [CreatureEntity] to be inserted.
     */
    suspend fun insertCreature(creatures: CreatureEntity)

    /**
     * Retrieves the name of a creature based on its ID.
     *
     * @param creatureId The ID of the creature.
     * @return The name of the creature, or null if not found.
     */
    suspend fun getCreatureName(creatureId: Int): String?

    /**
     * Retrieves the rarity of a creature based on its ID.
     *
     * @param creatureId The ID of the creature.
     * @return The rarity of the creature, or null if not found.
     */
    suspend fun getCreatureRarity(creatureId: Int): String?

    /**
     * Retrieves the hatch time required for a creature based on its ID.
     *
     * @param creatureId The ID of the creature.
     * @return The hatch time in milliseconds, or null if not found.
     */
    suspend fun getCreatureHatchTime(creatureId: Int): Long?

    /**
     * Retrieves all creatures from the data source.
     *
     * @return A list of all [CreatureEntity] objects.
     */
    suspend fun getAllCreatures(): List<CreatureEntity>
}