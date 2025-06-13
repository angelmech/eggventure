package com.example.eggventure.model.creature

/** * Interface for managing creature data.
 * This interface defines methods to retrieve and manipulate creature data.
 */
interface CreatureDataInterface {

    /**
     * Gets all creatures from the data source.
     */
    fun getAllCreatures(): List<Creature>


    /**
     * gets a creature by its ID from the data source.
     *
     * @param id the ID of the creature to retrieve.
     */
    fun getById(id: Int): Creature?


    /**
     * Gets a creature by its name from the data source.
     *
     * @param name the name of the creature to retrieve.
     */
    fun getByName(name: String): Creature?
}