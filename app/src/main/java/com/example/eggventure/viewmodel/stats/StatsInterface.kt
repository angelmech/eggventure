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
     * Retrieves the average steps taken per week.
     * @return A StateFlow emitting the average number of steps as a Double.
     */
    val weeklyAverageSteps: StateFlow<Double>

    /**
     * Retrieves the average distance covered per week.
     * @return A StateFlow emitting the average distance as a Double.
     */
    val weeklyAverageDistance: StateFlow<Double>

    /**
     * Formats the duration from milliseconds into a human-readable format (hh:mm:ss).
     * @param durationMillis The duration in milliseconds.
     * @return A formatted string representing the duration.
     */
    fun formatDuration(durationMillis: Long): String

    /**
     * Formats the distance from meters into kilometers with two decimal places.
     * @param distanceMeters The distance in meters.
     * @return A formatted string representing the distance in kilometers.
     */
    fun formatDistanceKm(distanceMeters: Float?): String

    /**
     * Formats the average speed into kilometers per hour with two decimal places.
     * @param averageSpeed The average speed.
     * @return A formatted string representing the average speed in kilometers per hour.
     */
    fun formatAverageSpeed(averageSpeed: Float?): String
}