package com.example.eggventure.viewmodel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import androidx.lifecycle.*
import com.example.eggventure.model.repository.HatchProgressRepository
import com.example.eggventure.model.repository.RunRepository
import com.example.eggventure.model.entity.HatchProgressEntity
import com.example.eggventure.model.entity.RunEntity
import com.example.eggventure.utils.StepSensorManager
import kotlinx.coroutines.launch

class StepCounterViewModel(
    private val stepSensorManager: StepSensorManager,
    private val hatchProgressRepository: HatchProgressRepository,
    private val runRepository: RunRepository
) : ViewModel(), SensorEventListener {

    private val _stepCount = MutableLiveData(0)      // This shows the hatch progress steps in UI
    val stepCount: LiveData<Int> = _stepCount

    private val _isTracking = MutableLiveData(false)
    val isTracking: LiveData<Boolean> = _isTracking

    private val _eggHatched = MutableLiveData<Boolean>()
    val eggHatched: LiveData<Boolean> = _eggHatched

    private var hatchId: Int? = null
    private var initialTotalSteps: Int? = null

    // Separate counters
    private var hatchProgressSteps: Int = 0   // Steps counted toward hatching egg, resets at goal
    private var runSteps: Int = 0             // Steps counted during the current run, never reset mid-run

    private var hatchGoal: Int = 5000 // can be changed trough database

    fun initProgress() {
        viewModelScope.launch {
            val lastProgress = hatchProgressRepository.getLastHatchProgress()
            if (lastProgress != null) {
                hatchId = lastProgress.id
                hatchProgressSteps = lastProgress.hatchProgressSteps
                hatchGoal = lastProgress.hatchGoal
                _stepCount.postValue(hatchProgressSteps)
            } else {
                val newProgress = HatchProgressEntity(
                    hatchProgressSteps = 0,
                    hatchGoal = hatchGoal
                )
                hatchProgressRepository.insertProgress(newProgress)
                val freshProgress = hatchProgressRepository.getLastHatchProgress()
                hatchId = freshProgress?.id
                hatchProgressSteps = freshProgress?.hatchProgressSteps ?: 0
                hatchGoal = freshProgress?.hatchGoal ?: hatchGoal
                _stepCount.postValue(hatchProgressSteps)
            }
        }
    }

    fun startStepTracking() {
        if (_isTracking.value == true) return
        stepSensorManager.registerListener(this)
        runSteps = 0
        _isTracking.postValue(true)
        _eggHatched.postValue(false)  // Reset hatch event
    }

    fun stopStepTracking() {
        stepSensorManager.unregisterListener()
        _isTracking.postValue(false)

        viewModelScope.launch {
            // Save the run with total runSteps counted so far
            val run = RunEntity(
                steps = runSteps,
                duration = 0L,
                date = System.currentTimeMillis(),
                averageSpeed = null,
                distanceMeters = null
            )
            runRepository.insertRun(run)

            // Save hatch progress to DB as is
            hatchId?.let { id ->
                hatchProgressRepository.updateHatchProgress(id, hatchProgressSteps)
            }
        }
    }

    /**
     * This method simulates steps, useful for testing.
     */
    fun addFakeStep(fakeStepCount: Int = 123) {
        // Increase both counters
        runSteps += fakeStepCount
        hatchProgressSteps += fakeStepCount

        // Check for hatch
        if (hatchProgressSteps >= hatchGoal) {
            val stepsAtHatch = initialTotalSteps?.plus(runSteps) ?: runSteps
            hatchEgg(stepsAtHatch)
        } else {
            _stepCount.postValue(hatchProgressSteps)
            // Save progress to DB
            viewModelScope.launch {
                hatchId?.let { id ->
                    hatchProgressRepository.updateHatchProgress(id, hatchProgressSteps)
                }
            }
        }
    }

    private fun hatchEgg(totalSteps: Int) {
        viewModelScope.launch {
            hatchId?.let { id ->
                hatchProgressRepository.updateHatchProgress(id, 0)
                hatchProgressSteps = 0
                initialTotalSteps = totalSteps
                _stepCount.postValue(0)
                _eggHatched.postValue(true)
                Log.d("StepCounter", "🎉 Egg hatched! Progress reset.")
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()

            if (initialTotalSteps == null) {
                initialTotalSteps = totalSteps - hatchProgressSteps
                Log.d("StepCounter", "Initial total steps set to $initialTotalSteps")
            }

            // Calculate steps since tracking started
            val stepsSinceStart = totalSteps - (initialTotalSteps ?: totalSteps)

            // Update counters
            runSteps = stepsSinceStart

            val newHatchProgress = hatchProgressSteps + (runSteps - (hatchProgressSteps))
            hatchProgressSteps = newHatchProgress.coerceAtMost(hatchGoal) // prevent overflow

            _stepCount.postValue(hatchProgressSteps)

            // Hatch if goal reached
            if (hatchProgressSteps >= hatchGoal) {
                hatchEgg(totalSteps)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        stepSensorManager.unregisterListener()
    }
}

