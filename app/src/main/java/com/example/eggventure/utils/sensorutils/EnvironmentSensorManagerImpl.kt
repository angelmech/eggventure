package com.example.eggventure.utils.sensorutils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class EnvironmentSensorManagerImpl(private val context: Context) : EnvironmentSensorManager {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private var light: Float? = null
    private var callback: ((Float?) -> Unit)? = null

    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (event.sensor.type == Sensor.TYPE_LIGHT) {
                    light = event.values[0]
                    Log.d("EnvSensor", "Light value: $light")
                    callback?.invoke(light)
                    stopReading()
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun stopReading() {
        Log.d("EnvSensor", "stopReading called, unregistering listener")
        sensorManager.unregisterListener(sensorListener)
        callback = null
    }

    override suspend fun readOnce(): Float? = suspendCancellableCoroutine { cont ->
        val oneShotListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
                    val value = event.values[0]
                    Log.d("EnvSensor", "readOnce Light: $value")
                    sensorManager.unregisterListener(this)
                    cont.resume(value)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.let {
            sensorManager.registerListener(oneShotListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            Log.d("EnvSensor", "Light sensor not available, resuming with null")
            cont.resume(null)
        }

        cont.invokeOnCancellation {
            Log.d("EnvSensor", "readOnce: Coroutine cancelled, unregistering oneShotListener")
            sensorManager.unregisterListener(oneShotListener)
        }
    }
}
