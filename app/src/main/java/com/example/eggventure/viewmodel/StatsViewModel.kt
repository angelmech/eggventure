package com.example.eggventure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eggventure.model.run.RunEntity
import com.example.eggventure.model.run.RunRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class StatsViewModel(private val runRepository: RunRepository) : ViewModel() {

    private val _allRuns = MutableStateFlow<List<RunEntity>>(emptyList())
    val allRuns: StateFlow<List<RunEntity>> = _allRuns

    private val _lastRun = MutableStateFlow<RunEntity?>(null)
    val lastRun: StateFlow<RunEntity?> = _lastRun

    private val _weeklyAverageSteps = MutableStateFlow<Double>(0.0)
    val weeklyAverageSteps: StateFlow<Double> = _weeklyAverageSteps

    private val _weeklyAverageDistance = MutableStateFlow<Double>(0.0)
    val weeklyAverageDistance: StateFlow<Double> = _weeklyAverageDistance

    init {
        viewModelScope.launch {
            runRepository.getAllRuns().collect { runs ->
                _allRuns.value = runs
            }
        }
        viewModelScope.launch {
            _lastRun.value = runRepository.getLastRun()
        }
        viewModelScope.launch {
            val weekAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
            _weeklyAverageSteps.value = runRepository.getWeeklyAverage(weekAgo)
        }
        viewModelScope.launch {
            val weekAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
            _weeklyAverageDistance.value = runRepository.getWeeklyAverageDistance(weekAgo)
        }
    }

    // Formatiert die Dauer zu hh:mm:ss
    fun formatDuration(durationMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(durationMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // Formatiert die Distanz zu km (mit 2 Nachkommastellen)
    fun formatDistanceKm(distanceMeters: Float?): String {
        return distanceMeters?.let { String.format("%.2f km", it / 1000f) } ?: "N/A"
    }

    // Formatiert die Durchschnittsgeschwindigkeit zu km/h
    fun formatAverageSpeed(averageSpeed: Float?): String {
        return averageSpeed?.let { String.format("%.2f km/h", it) } ?: "N/A"
    }
}
