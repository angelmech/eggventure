package com.example.eggventure.model.hatchprogress

/**
 * Repository interface for managing hatch progress data.
 */
interface HatchProgressRepository {
    /**
     * Inserts a new hatch progress entry into the data source.
     *
     * @param progress The [HatchProgressEntity] to be inserted.
     */
    suspend fun insertProgress(progress: HatchProgressEntity)

    /**
     * Retrieves the most recent hatch progress entry.
     *
     * @return The last [HatchProgressEntity], or null if no progress is found.
     */
    suspend fun getLastHatchProgress(): HatchProgressEntity?

    /**
     * Updates an existing hatch progress entry with new accumulated steps.
     *
     * @param id The ID of the hatch progress entry to update.
     * @param stepsAccumulated The new total accumulated steps.
     */
    suspend fun updateHatchProgress(id: Int, stepsAccumulated: Int)
}