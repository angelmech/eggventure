package com.example.eggventure.model.run
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing run data.
 */
interface RunRepository {
    /**
     * Inserts a new run into the data source.
     *
     * @param run The [RunEntity] to be inserted.
     */
    suspend fun insertRun(run: RunEntity)

    /**
     * Retrieves all runs from the data source as a Flow.
     * The Flow will emit a new list of runs whenever the underlying data changes.
     *
     * @return A Flow emitting a list of [RunEntity] objects.
     */
    fun getAllRuns(): Flow<List<RunEntity>>

    /**
     * Retrieves the most recent run from the data source.
     *
     * @return The last [RunEntity], or null if no runs are found.
     */
    suspend fun getLastRun(): RunEntity?

    suspend fun getLast7Runs(): List<RunEntity>
}