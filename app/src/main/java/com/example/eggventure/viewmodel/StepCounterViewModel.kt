package com.example.eggventure.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eggventure.utils.StepSensorManager
import kotlinx.coroutines.launch

class StepCounterViewModel(
    private val context: Context
) : ViewModel(), SensorEventListener {

    private lateinit var stepSensorManager: StepSensorManager
    private val _stepCount = MutableLiveData(0)
    val stepCount: LiveData<Int> = _stepCount

    fun startStepTracking() {
        stepSensorManager = StepSensorManager(context)
        stepSensorManager.registerListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val steps = event.values[0].toInt()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        stepSensorManager.unregisterListener()
    }
}

