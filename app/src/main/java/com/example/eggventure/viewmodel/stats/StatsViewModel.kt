package com.example.eggventure.viewmodel.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eggventure.model.run.RunEntity
import com.example.eggventure.model.run.RunRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class StatsViewModel(private val runRepository: RunRepository) : ViewModel() {

    private val _allRuns = MutableStateFlow<List<RunEntity>>(emptyList())
    val allRuns: StateFlow<List<RunEntity>> = _allRuns

    private val _lastRun = MutableStateFlow<RunEntity?>(null)
    val lastRun: StateFlow<RunEntity?> = _lastRun

    private val _last7Runs = MutableStateFlow<List<RunEntity>>(emptyList())
    val last7Runs: StateFlow<List<RunEntity>> = _last7Runs

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
            _last7Runs.value = runRepository.getLast7Runs()
            Log.d("StatsViewModel", "Last 7 runs retrieved: ${_last7Runs.value}")
        }
    }

    // Formatiert die Dauer zu hh:mm:ss
    fun formatDuration(durationMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(durationMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // Formatiert den Timestamp in ein lesbares Datum
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
