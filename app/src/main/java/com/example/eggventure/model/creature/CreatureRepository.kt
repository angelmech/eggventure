package com.example.eggventure.model.creature

import kotlinx.coroutines.flow.Flow

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
     * Retrieves all creatures from the data source.
     *
     * @return A list of all [CreatureEntity] objects.
     */
    fun getAllCreatures(): Flow<List<CreatureEntity>>
}