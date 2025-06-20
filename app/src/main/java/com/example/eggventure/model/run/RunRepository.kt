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

    /**
     * Retrieves the date of the last run.
     *
     * @return The timestamp (in milliseconds) of the last run, or null if no runs are found.
     */
    suspend fun getLastRunDate(): Long?

    /**
     * Retrieves the duration of the last run.
     *
     * @return The duration (in milliseconds) of the last run, or null if no runs are found.
     */
    suspend fun getLastRunDuration(): Long?

    /**
     * Retrieves the distance of the last run.
     *
     * @return The distance (in meters) of the last run, or null if no runs are found.
     */
    suspend fun getLastRunDistance(): Float?

    /**
     * Retrieves the average speed of the last run.
     *
     * @return The average speed (in meters per second) of the last run, or null if no runs are found.
     */
    suspend fun getLastRunAverageSpeed(): Float?

    /**
     * Retrieves the number of steps taken during the last run.
     *
     * @return The total steps of the last run, or null if no runs are found.
     */
    suspend fun getLastRunSteps(): Int?

    //suspend fun getWeeklyAverage(weekAgo: Long): Double

    //suspend fun getWeeklyAverageDistance(weekAgo: Long): Double

}