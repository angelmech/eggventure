package com.example.eggventure.viewmodel

import androidx.lifecycle.*
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import kotlinx.coroutines.launch

class StepCounterImpl(
    private val trackingService: StepTrackingService,
    private val hatchEvent: EggHatchEvent,
    private val runPersistence: RunPersistence,
    private val hatchProgressRepository: HatchProgressRepository
) : ViewModel(), StepCounter {

    private val _stepCount = MutableLiveData(0)
    override val stepCount: LiveData<Int> = _stepCount

    private val _isTracking = MutableLiveData(false)
    override val isTracking: LiveData<Boolean> = _isTracking

    private val _eggHatched = MutableLiveData(false)
    override val eggHatched: LiveData<Boolean> = _eggHatched

    private var hatchId: Int? = null
    private var hatchGoal: Int = 5000
    private var currentSteps: Int = 0

    override fun initProgress() {
        viewModelScope.launch {
            val progress = hatchProgressRepository.getLastHatchProgress()
            if (progress != null) {
                hatchId = progress.id
                hatchGoal = progress.hatchGoal
                currentSteps = progress.hatchProgressSteps
                _stepCount.postValue(currentSteps)
            }
        }
    }

    override fun startTracking() {
        if (_isTracking.value == true) return
        _isTracking.postValue(true)
        _eggHatched.postValue(false)

        trackingService.start { deltaSteps ->
            viewModelScope.launch {
                currentSteps += deltaSteps
                val hatched = hatchId?.let { id ->
                    hatchEvent.processHatch(id, currentSteps, hatchGoal)
                } ?: false
                _stepCount.postValue(if (hatched) 0 else currentSteps)
                if (hatched) {
                    currentSteps = 0
                    _eggHatched.postValue(true)
                }
            }
        }
    }

    override fun stopTracking() {
        _isTracking.postValue(false)
        trackingService.stop()
        viewModelScope.launch {
            runPersistence.saveRun(currentSteps)
            hatchId?.let { hatchProgressRepository.updateHatchProgress(it, currentSteps) }
        }
    }

    override fun addFakeStep(fakeStepCount: Int) {
        viewModelScope.launch {
            currentSteps += fakeStepCount
            val hatched = hatchId?.let { id ->
                hatchEvent.processHatch(id, currentSteps, hatchGoal)
            } ?: false
            _stepCount.postValue(if (hatched) 0 else currentSteps)
            if (hatched) {
                currentSteps = 0
                _eggHatched.postValue(true)
            }
        }
    }

    override fun onCleared() {
        trackingService.stop()
    }
}

