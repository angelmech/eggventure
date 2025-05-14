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
    private val _isTracking = MutableLiveData(false)
    val isTracking: LiveData<Boolean> = _isTracking
    private var initialStepValue: Int? = null


    fun startStepTracking() {
        if (!isTracking.value!!) {
            stepSensorManager = StepSensorManager(context)
            stepSensorManager.registerListener(this)
            _isTracking.postValue(true)
        }
    }

    fun stopStepTracking() {
        stepSensorManager.unregisterListener()
        _isTracking.postValue(false)
    }

    /**
     * Simulate a step for testing purposes.
     * This method should be removed in production code.
     */
    fun addFakeStep() {
        val current = _stepCount.value ?: 0
        _stepCount.postValue(current + 1)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()
            Log.d("StepCounter", "Sensor event received, totalSteps: $totalSteps")

            if (initialStepValue == null) {
                initialStepValue = totalSteps
                Log.d("StepCounter", "Initial step value set to $initialStepValue")
            }

            val currentSteps = totalSteps - (initialStepValue ?: totalSteps)
            Log.d("StepCounter", "Current steps since tracking started: $currentSteps")

            _stepCount.postValue(currentSteps)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        stepSensorManager.unregisterListener()
    }
}

