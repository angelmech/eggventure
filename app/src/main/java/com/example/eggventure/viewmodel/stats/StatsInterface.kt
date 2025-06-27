package com.example.eggventure.viewmodel.stats

import com.example.eggventure.model.run.RunEntity
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for managing and retrieving statistics related to runs.
 */
interface StatsInterface {
    /**
     * Retrieves all recorded runs.
     * @return A StateFlow emitting a list of RunEntity objects.
     */
    val allRuns: StateFlow<List<RunEntity>>

    /**
     * Retrieves the most recent run.
     * @return A StateFlow emitting the last RunEntity object, or null if no runs have been recorded.
     */
    val lastRun: StateFlow<RunEntity?>

    /**
     * Retrieves the last 7 recorded runs.
     * @return A StateFlow emitting a list of the last 7 RunEntity objects.
     */
    val last7Runs: StateFlow<List<RunEntity>>

    /**
     * Formats the duration from milliseconds into a human-readable format (hh:mm:ss).
     * @param durationMillis The duration in milliseconds.
     * @return A formatted string representing the duration.
     */
    fun formatDuration(durationMillis: Long): String

    /**
     * Formats the timestamp into a readable date string.
     * @param timestamp The timestamp in milliseconds.
     * @param dateOnly Whether to only include the date (default: false).
     */
    fun formatDate(timestamp: Long, dateOnly: Boolean = false): String

}