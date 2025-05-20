package com.example.eggventure.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.model.dao.HatchProgressDao
import com.example.eggventure.model.dao.RunDao
import com.example.eggventure.model.repository.HatchProgressRepository
import com.example.eggventure.model.repository.RunRepository
import com.example.eggventure.utils.StepSensorManager
import com.example.eggventure.utils.StepSensorManagerImpl

/**
 * Factory class to create an instance of StepCounterViewModel.
 */
class StepCounterViewModelFactory(
    private val context: Context,
    private val hatchProgressRepository: HatchProgressRepository,
    private val runRepository: RunRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val stepSensorManager = StepSensorManagerImpl(context)
        return StepCounterViewModel(stepSensorManager, hatchProgressRepository, runRepository) as T
    }
}

