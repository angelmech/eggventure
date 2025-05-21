package com.example.eggventure.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.model.AppDatabase
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import com.example.eggventure.model.run.RunRepository
import com.example.eggventure.model.run.RunRepositoryImpl
import com.example.eggventure.utils.StepSensorManagerImpl

/**
 * Factory class for creating instances of [StepCounterImpl].
 */
class StepCounterFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of [StepCounterImpl] with the required dependencies.
     *
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(context)

        val hatchProgressRepository = HatchProgressRepository(db.hatchProgressDao())
        val runRepository : RunRepository = RunRepositoryImpl(db.runDao())

        val eggHatchEvent = EggHatchEvent(hatchProgressRepository)
        val runPersistence = RunPersistence(runRepository)
        val stepTrackingService = StepTrackingService(StepSensorManagerImpl(context))

        @Suppress("UNCHECKED_CAST")
        return StepCounterImpl(
            stepTrackingService,
            eggHatchEvent,
            runPersistence,
            hatchProgressRepository
        ) as T
    }
}


