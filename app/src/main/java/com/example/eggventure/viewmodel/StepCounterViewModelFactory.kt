package com.example.eggventure.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.utils.StepSensorManager
import com.example.eggventure.utils.StepSensorManagerImpl

class StepCounterViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val stepSensorManager = StepSensorManagerImpl(context)
        return StepCounterViewModel(stepSensorManager) as T
    }
}

