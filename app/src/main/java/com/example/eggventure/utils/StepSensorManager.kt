package com.example.eggventure.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class StepSensorManager(private val context: Context) {

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var listener: SensorEventListener? = null

    fun registerListener(listener: SensorEventListener) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Log.e("StepSensorManager", "Step counter sensor not available.")
            return
        }

        this.listener = listener
        sensorManager?.registerListener(listener, stepSensor, SensorManager.SENSOR_DELAY_UI)
        Log.d("StepSensorManager", "Step counter sensor registered.")
    }

    fun unregisterListener() {
        listener?.let {
            sensorManager?.unregisterListener(it)
            Log.d("StepSensorManager", "Step counter sensor unregistered.")
        }
        listener = null
    }
}
