package com.example.eggventure.utils.sensorutils

/* * Interface for managing environment sensors like temperature and light.
 * This interface defines methods to start and stop reading sensor values.
 */
interface EnvironmentSensorManager {
    /**
     * Starts reading temperature and light sensor values.
     *
     * @param onValuesReady Callback function that is called with the temperature and light values
     *                      when they are ready. Both values can be null if the sensors are not available.
     */
    fun startReading(onValuesReady: (temperature: Float?, light: Float?) -> Unit)

    /**
     * Stops reading sensor values.
     * This method should be called when the sensor readings are no longer needed to free up resources.
     */
    fun stopReading()

    /**
     * Coroutine-based method to read the temperature and light sensor values once.
     *
     * @return A pair containing the temperature and light values. Both can be null if the sensors are not available.
     */
    suspend fun readOnce(): Pair<Float, Float>
}
