package com.example.eggventure.viewmodel.stepcounter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.model.AppDatabase
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import com.example.eggventure.model.hatchprogress.HatchProgressRepositoryImpl
import com.example.eggventure.model.run.RunRepository
import com.example.eggventure.model.run.RunRepositoryImpl
import com.example.eggventure.utils.sensorutils.StepSensorManager
import com.example.eggventure.utils.sensorutils.StepSensorManagerImpl

/**
 * Factory class for creating instances of [StepCounter].
 */
class StepCounterFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of [StepCounter] with the required dependencies.
     *
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(context)

        val hatchProgressRepository : HatchProgressRepository = HatchProgressRepositoryImpl(db.hatchProgressDao())
        val runRepository : RunRepository = RunRepositoryImpl(db.runDao())

        val eggHatchEvent = EggHatchEvent(hatchProgressRepository)
        val runPersistence = RunPersistence(runRepository)
        val stepSensorManager : StepSensorManager = StepSensorManagerImpl(context)

        @Suppress("UNCHECKED_CAST")
        return StepCounter(
            stepSensorManager,
            eggHatchEvent,
            runPersistence,
            hatchProgressRepository
        ) as T
    }
}


