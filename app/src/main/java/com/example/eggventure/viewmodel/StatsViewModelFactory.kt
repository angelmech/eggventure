package com.example.eggventure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.model.run.RunRepository

class StatsViewModelFactory(private val runRepository: RunRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            return StatsViewModel(runRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
