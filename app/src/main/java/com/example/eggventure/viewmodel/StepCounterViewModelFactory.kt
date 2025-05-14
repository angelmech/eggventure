package com.example.eggventure.viewmodel

import android.content.Context
import android.hardware.SensorEventListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.utils.StepSensorManager

class StepCounterViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StepCounterViewModel(context) as T
    }
}

