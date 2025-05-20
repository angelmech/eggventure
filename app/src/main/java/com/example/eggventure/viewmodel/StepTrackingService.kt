package com.example.eggventure.viewmodel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import com.example.eggventure.utils.StepSensorManager

class StepTrackingService(
    private val stepSensorManager: StepSensorManager
) : SensorEventListener {

    private var listener: ((Int) -> Unit)? = null
    private var initialSteps: Int? = null

    fun start(onStep: (Int) -> Unit) {
        listener = onStep
        stepSensorManager.registerListener(this)
    }

    fun stop() {
        stepSensorManager.unregisterListener()
        listener = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()
            if (initialSteps == null) initialSteps = totalSteps
            val deltaSteps = totalSteps - (initialSteps ?: totalSteps)
            listener?.invoke(deltaSteps)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
