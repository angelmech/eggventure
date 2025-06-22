package com.example.eggventure.utils.sensorutils

import kotlinx.coroutines.flow.Flow

/* * Interface for managing environment sensors like light.
 * This interface defines methods to start and stop reading sensor values.
 */
interface EnvironmentSensorManager {
    /**
     * Stops reading sensor values.
     * This method should be called when the sensor readings are no longer needed to free up resources.
     */
    fun stopReading()

    /**
     * Coroutine-based method to read the light sensor values once.
     *
     * @return A pair containing the temperature and light values. Both can be null if the sensors are not available.
     */
    suspend fun readOnce(): Float?

    /**
     * Observes the light sensor values as a Flow.
     * This method allows continuous observation of light sensor values.
     *
     * @return A Flow emitting light sensor values.
     */
    fun observeLight(): Flow<Float>
}
