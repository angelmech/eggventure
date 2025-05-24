package com.example.eggventure.utils.sensorutils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class StepSensorManagerImpl(private val context: Context) : StepSensorManager {

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var listener: SensorEventListener? = null

    override fun registerListener(listener: SensorEventListener) {
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

    override fun unregisterListener() {
        listener?.let {
            sensorManager?.unregisterListener(it)
            Log.d("StepSensorManager", "Step counter sensor unregistered.")
        }
        listener = null
    }
}
