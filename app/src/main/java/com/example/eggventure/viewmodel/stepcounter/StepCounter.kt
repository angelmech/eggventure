package com.example.eggventure.viewmodel.stepcounter

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import androidx.lifecycle.*
import com.example.eggventure.model.hatchprogress.HatchProgressEntity
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import com.example.eggventure.utils.sensorutils.EnvironmentSensorManager
import com.example.eggventure.utils.sensorutils.StepSensorManager
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogicInterface
import com.example.eggventure.viewmodel.creaturelogic.EggHatchEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StepCounter(
    private val stepSensorManager: StepSensorManager,
    private val runPersistence: RunPersistence,
    private val hatchProgressRepository: HatchProgressRepository,
    private val creatureLogic: CreatureLogicInterface,
    private val environmentSensorManager: EnvironmentSensorManager
) : ViewModel(), SensorEventListener, StepCounterInterface {

    private val _stepCount = MutableLiveData(0)
    override val stepCount: LiveData<Int> = _stepCount

    private val _isTracking = MutableLiveData(false)
    override val isTracking: LiveData<Boolean> = _isTracking

    private val _eggHatched = MutableLiveData(false)
    override val eggHatched: LiveData<Boolean> = _eggHatched

    private var hatchId: Int? = null
    private var hatchGoal: Int = 5000
    private var hatchProgressSteps: Int = 0
    private var runSteps: Int = 0
    private var fakeStepOffset: Int = 0
    private var initialTotalSteps: Int? = null

    private val _currentLightLevel = MutableStateFlow<Float?>(null)
    val currentLightLevel: StateFlow<Float?> = _currentLightLevel

    init {
        viewModelScope.launch {
            environmentSensorManager.observeLight().collect { lux ->
                _currentLightLevel.value = lux
            }
        }
    }

    override fun initProgress() {
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

    override fun startTracking() {
        if (_isTracking.value == true) return
        stepSensorManager.registerListener(this)
        runSteps = 0
        _isTracking.postValue(true)
        _eggHatched.postValue(false)
    }

    override fun stopTracking() {
        stepSensorManager.unregisterListener()
        _isTracking.postValue(false)

        viewModelScope.launch {
            runPersistence.saveRun(runSteps)
            hatchId?.let { id ->
                hatchProgressRepository.updateHatchProgress(id, hatchProgressSteps) }
        }
    }


    override fun addFakeStep(fakeSteps: Int) {
        fakeStepOffset += fakeSteps
        hatchProgressSteps += fakeSteps

        if (hatchProgressSteps >= hatchGoal) {
            val stepsAtHatch = initialTotalSteps?.plus(runSteps) ?: runSteps
            startEggHatchEvent(stepsAtHatch)
        } else {
            _stepCount.postValue(hatchProgressSteps)
            viewModelScope.launch {
                hatchId?.let { id ->
                    hatchProgressRepository.updateHatchProgress(id, hatchProgressSteps)
                }
            }
        }
    }

    /* * Starts the egg hatch event if the progress reaches the goal.
     * This function is called when the step count reaches or exceeds the hatch goal.
     */
    private fun startEggHatchEvent(totalSteps: Int) {
        viewModelScope.launch {
            hatchId?.let { id ->
                //val hatched = hatchEvent.processHatch(id, hatchProgressSteps, hatchGoal)
                // call the creatureLogic viewmodel to handle the egg hatch logic
                val hatched = creatureLogic.hatchCreature(
                    id,
                    totalSteps,
                    hatchProgressSteps,
                    hatchGoal,
                    _currentLightLevel.value ?: 0f)
                if (hatched) {
                    hatchProgressSteps = 0
                    fakeStepOffset = 0
                    initialTotalSteps = totalSteps
                    _stepCount.postValue(0)
                    _eggHatched.postValue(true)
                    Log.d("StepCounter", "Egg hatched! Progress reset.")
                }
            }
        }
    }

    /**
     * Updates the step count and checks if the hatch goal is reached.
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()

            if (initialTotalSteps == null) {
                initialTotalSteps = totalSteps - hatchProgressSteps
                Log.d("StepCounter", "Initial total steps set to $initialTotalSteps")
            }

            Log.d("StepCounter", "step")

            // Real steps since tracking started
            val realSteps = totalSteps - (initialTotalSteps ?: totalSteps)
            runSteps = realSteps

            // Combine real + fake steps
            val combinedSteps = realSteps + fakeStepOffset
            hatchProgressSteps = combinedSteps.coerceAtMost(hatchGoal)

            _stepCount.postValue(hatchProgressSteps)

            if (hatchProgressSteps >= hatchGoal) {
                startEggHatchEvent(totalSteps)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        stepSensorManager.unregisterListener()
    }
}
