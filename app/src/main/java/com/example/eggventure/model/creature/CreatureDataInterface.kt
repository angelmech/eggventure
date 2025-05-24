package com.example.eggventure.model.creature

/** * Interface for managing creature data.
 * This interface defines methods to retrieve and manipulate creature data.
 */
interface CreatureDataInterface {

    /**
     * Inserts a new creature into the data source.
     *
     * @param creature The [Creature] to be inserted.
     */
    fun getAllCreatures(): List<Creature>


    /**
     * Inserts a new creature into the data source.
     *
     * @param creature The [Creature] to be inserted.
     */
    fun getById(id: Int): Creature?
}