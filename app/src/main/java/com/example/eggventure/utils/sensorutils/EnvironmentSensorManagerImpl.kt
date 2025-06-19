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

    private var temperature: Float? = null
    private var light: Float? = null
    private var callback: ((Float?, Float?) -> Unit)? = null

    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                when (event.sensor.type) {
                    Sensor.TYPE_AMBIENT_TEMPERATURE -> temperature = event.values[0]
                    Sensor.TYPE_LIGHT -> light = event.values[0]
                }

                if (temperature != null && light != null) {
                    Log.d("EnvSensor", "Both values ready, invoking callback")
                    callback?.invoke(temperature, light)
                    stopReading()
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun startReading(onValuesReady: (Float?, Float?) -> Unit) {
        Log.d("EnvSensor", "startReading called")
        callback = onValuesReady

        sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)?.let {
            sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.let {
            sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun stopReading() {
        Log.d("EnvSensor", "stopReading called, unregistering listener")
        sensorManager.unregisterListener(sensorListener)
        callback = null
    }

    override suspend fun readOnce(): Pair<Float, Float> = suspendCancellableCoroutine { cont ->
        var temp: Float? = null
        var light: Float? = null

        val oneShotListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    Log.d("EnvSensor", "readOnce onSensorChanged: type=${event.sensor.type}, value=${event.values[0]}")
                    when (event.sensor.type) {
                        Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                            temp = event.values[0]
                            Log.d("EnvSensor", "readOnce Temperature: $temp")
                        }
                        Sensor.TYPE_LIGHT -> {
                            light = event.values[0]
                            Log.d("EnvSensor", "readOnce Light: $light")
                        }
                    }

                    if (temp != null && light != null) {
                        Log.d("EnvSensor", "readOnce: Both values ready, resuming coroutine")
                        sensorManager.unregisterListener(this)
                        cont.resume(Pair(temp!!, light!!))
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)?.let {
            sensorManager.registerListener(oneShotListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.let {
            sensorManager.registerListener(oneShotListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        cont.invokeOnCancellation {
            Log.d("EnvSensor", "readOnce: Coroutine cancelled, unregistering oneShotListener")
            sensorManager.unregisterListener(oneShotListener)
        }
    }
}
