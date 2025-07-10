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


class Stats(private val runRepository: RunRepository) : ViewModel(), StatsInterface {

    private val _allRuns = MutableStateFlow<List<RunEntity>>(emptyList())
    override val allRuns: StateFlow<List<RunEntity>> = _allRuns

    private val _lastRun = MutableStateFlow<RunEntity?>(null)
    override val lastRun: StateFlow<RunEntity?> = _lastRun

    private val _last7Runs = MutableStateFlow<List<RunEntity>>(emptyList())
    override val last7Runs: StateFlow<List<RunEntity>> = _last7Runs

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
            runRepository.getLast7Runs().collect { runs ->
                _last7Runs.value = runs
            }
        }
    }

    // Formatiert die Dauer zu hh:mm:ss
    override fun formatDuration(durationMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(durationMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // Formatiert den Timestamp in ein lesbares Datum
    override fun formatDate(timestamp: Long, dateOnly: Boolean): String {
        if (dateOnly) {
            val sdf = SimpleDateFormat("dd.MM", Locale.getDefault())
            return sdf.format(Date(timestamp))
        } else {
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
}
