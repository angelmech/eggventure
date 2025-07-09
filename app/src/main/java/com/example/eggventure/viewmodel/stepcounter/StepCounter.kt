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

    private val _currentLightLevel = MutableStateFlow<Float?>(null)
    val currentLightLevel: StateFlow<Float?> = _currentLightLevel

    private var hatchId: Int? = null
    private var hatchGoal: Int = 5000
    private var hatchProgressSteps: Int = 0
    private var runSteps: Int = 0
    private var lastSensorSteps: Int? = null
    private var runStartTime: Long = 0L


    init {
        viewModelScope.launch {
            environmentSensorManager.observeLight().collect { lux ->
                _currentLightLevel.value = lux
            }
        }
    }

    //-------------------------initializations--------------------------------

    override fun initProgress() {
        viewModelScope.launch {
            try {
                val lastProgress = hatchProgressRepository.getLastHatchProgress()
                if (lastProgress != null) {
                    hatchId = lastProgress.id
                    hatchProgressSteps = lastProgress.hatchProgressSteps
                    hatchGoal = lastProgress.hatchGoal
                } else {
                    val newProgress = HatchProgressEntity(
                        hatchProgressSteps = 0,
                        hatchGoal = hatchGoal
                    )
                    hatchProgressRepository.insertProgress(newProgress)
                    val fresh = hatchProgressRepository.getLastHatchProgress()
                    hatchId = fresh?.id
                    hatchProgressSteps = fresh?.hatchProgressSteps ?: 0
                    hatchGoal = fresh?.hatchGoal ?: hatchGoal
                }
                _stepCount.postValue(hatchProgressSteps)
            } catch (e: Exception) {
                _stepCount.postValue(0)
            }
        }
    }

    override fun startTracking() {
        if (_isTracking.value == true) return

        runSteps = 0
        lastSensorSteps = null
        runStartTime = System.currentTimeMillis()

        _eggHatched.postValue(false)
        _stepCount.postValue(hatchProgressSteps)
        stepSensorManager.registerListener(this)
        _isTracking.postValue(true)
    }

    override fun stopTracking() {
        stepSensorManager.unregisterListener()
        _isTracking.postValue(false)

        val runDuration = System.currentTimeMillis() - runStartTime
        viewModelScope.launch {
            runPersistence.saveRun(runSteps, runDuration, runStartTime)
            hatchId?.let { hatchProgressRepository.updateHatchProgress(it, hatchProgressSteps) }
        }
    }

    //-------------------------Step count logic--------------------------------

    override fun addFakeStep(fakeSteps: Int) {
        runSteps += fakeSteps
        hatchProgressSteps += fakeSteps

        var overflow = 0
        if (hatchProgressSteps >= hatchGoal) {
            overflow = hatchProgressSteps - hatchGoal
            hatchProgressSteps = hatchGoal
        }

        checkHatchCondition(overflow)
    }

    /**
     * Handles the sensor change event.
     * This function is called when the step sensor detects a change in step count.
     *
     * @param event The SensorEvent containing the step count data.
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()
            onStepSensorDataChanged(StepSensorData(totalSteps))
        }
    }

    /**
     * Handles the step sensor data change.
     * This function is called when the step sensor detects a change in step count.
     *
     * @param data The StepSensorData containing the total steps.
     */
    fun onStepSensorDataChanged(data: StepSensorData) {
        val totalSteps = data.totalSteps

        // If first time, set the last sensor steps and exit
        if (lastSensorSteps == null) {
            lastSensorSteps = totalSteps
            return
        }

        val delta = totalSteps - lastSensorSteps!!
        if (delta > 0) {
            runSteps += delta
            hatchProgressSteps = (hatchProgressSteps + delta).coerceAtMost(hatchGoal)
            checkHatchCondition()
        }

        lastSensorSteps = totalSteps
    }

    //-------------------------Egg Hatch--------------------------------

    /**
     * Checks the hatch condition based on the current step count.
     */
    private fun checkHatchCondition(overflow: Int = 0) {
        if (hatchProgressSteps >= hatchGoal) {
            val totalSteps = (lastSensorSteps ?: 0) + runSteps
            startEggHatchEvent(totalSteps, overflow)
        } else {
            _stepCount.postValue(hatchProgressSteps)
        }
    }

    /**
     * Starts the egg hatch event
     *
     * @param totalSteps The total number of steps taken by the user.
     */
    private fun startEggHatchEvent(totalSteps: Int, overflow: Int = 0) {
        viewModelScope.launch {
            hatchId?.let { id ->
                val hatched = creatureLogic.hatchCreature(
                    id,
                    totalSteps,
                    hatchProgressSteps,
                    hatchGoal,
                    _currentLightLevel.value ?: 0f
                )
                if (hatched) {
                    hatchProgressSteps = overflow
                    _stepCount.postValue(hatchProgressSteps)
                    _eggHatched.postValue(true)
                } else {
                    _stepCount.postValue(hatchProgressSteps)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    public override fun onCleared() {
        stepSensorManager.unregisterListener()
    }
}
