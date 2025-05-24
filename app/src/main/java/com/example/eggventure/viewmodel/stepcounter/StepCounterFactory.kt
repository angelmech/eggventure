package com.example.eggventure.viewmodel.stepcounter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eggventure.model.AppDatabase
import com.example.eggventure.model.creature.CreatureDataInterface
import com.example.eggventure.model.creature.CreatureDatabase
import com.example.eggventure.model.creature.CreatureRepository
import com.example.eggventure.model.creature.CreatureRepositoryImpl
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import com.example.eggventure.model.hatchprogress.HatchProgressRepositoryImpl
import com.example.eggventure.model.run.RunRepository
import com.example.eggventure.model.run.RunRepositoryImpl
import com.example.eggventure.utils.sensorutils.StepSensorManager
import com.example.eggventure.utils.sensorutils.StepSensorManagerImpl
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogic
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogicFactory
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogicInterface
import com.example.eggventure.viewmodel.creaturelogic.EggHatchEvent

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
        val creatureRepository : CreatureRepository =  CreatureRepositoryImpl(db.creatureDao())

        val hatchEvent = EggHatchEvent(hatchProgressRepository, creatureRepository)
        val runPersistence = RunPersistence(runRepository)
        val stepSensorManager : StepSensorManager = StepSensorManagerImpl(context)

        val creatureData : CreatureDataInterface = CreatureDatabase
        val creatureLogic: CreatureLogicInterface = CreatureLogic(creatureData, hatchEvent)


        @Suppress("UNCHECKED_CAST")
        return StepCounter(
            stepSensorManager,
            runPersistence,
            hatchProgressRepository,
            creatureLogic
        ) as T
    }
}


