// StepSensorInterface.kt
package com.example.eggventure.utils

import android.hardware.SensorEventListener

/**
 * Interface for managing step sensor events.
 */
interface StepSensorManager {
    /**
     * Registers a listener for step sensor events.
     *
     * @param listener The listener to register.
     */
    fun registerListener(listener: SensorEventListener)

    /**
     * Unregisters the previously registered listener.
     */
    fun unregisterListener()
}