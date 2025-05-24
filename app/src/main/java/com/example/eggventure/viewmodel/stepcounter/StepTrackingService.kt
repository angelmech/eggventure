package com.example.eggventure.viewmodel.stepcounter

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import com.example.eggventure.utils.sensorutils.StepSensorManager

class StepTrackingService(
    private val stepSensorManager: StepSensorManager
) : SensorEventListener {

    private var listener: ((Int) -> Unit)? = null
    private var lastTotalSteps: Int? = null

    fun start(onStep: (Int) -> Unit) {
        listener = onStep
        lastTotalSteps = null
        stepSensorManager.registerListener(this)
    }

    fun stop() {
        stepSensorManager.unregisterListener()
        listener = null
        lastTotalSteps = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val currentTotalSteps = event.values[0].toInt()

            if (lastTotalSteps == null) {
                lastTotalSteps = currentTotalSteps
                return
            }

            val delta = currentTotalSteps - lastTotalSteps!!
            if (delta > 0) {
                listener?.invoke(delta)
                lastTotalSteps = currentTotalSteps
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
