package com.example.eggventure.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.app.ActivityCompat

class StepCounterInitializer(private val context: Context, private val listener: SensorEventListener) {

    private var sensorManager: SensorManager? = null
    private var stepCounterSensor: Sensor? = null
    private val tag = "StepCounterInitializer"

    fun initialize() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepCounterSensor == null) {
            Log.w(tag, "Step Counter sensor not available on this device.")
            return
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w(tag, "ACTIVITY_RECOGNITION permission not granted. Service might fail.")
                return
            } else {
                registerListener()
            }
        } else {
            registerListener()
        }
    }

    private fun registerListener() {
        try {
            sensorManager?.registerListener(listener, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
            Log.i(tag, "Step Counter listener registered via Initializer.")
        } catch (e: SecurityException) {
            Log.e(tag, "SecurityException while registering listener: ${e.message}")
            Log.w(tag, "Make sure you have the necessary permissions declared in your Manifest.")
        }
    }

    fun unregister() {
        sensorManager?.unregisterListener(listener)
        Log.i(tag, "Step Counter listener unregistered via Initializer.")
    }
}