package com.example.eggventure.utils

import android.content.Context
import com.example.eggventure.model.AppDatabase
import com.example.eggventure.model.creature.CreatureDataInterface
import com.example.eggventure.model.creature.CreatureDatabase
import com.example.eggventure.model.creature.CreatureRepository
import com.example.eggventure.model.creature.CreatureRepositoryImpl
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import com.example.eggventure.model.hatchprogress.HatchProgressRepositoryImpl
import com.example.eggventure.model.run.RunRepository
import com.example.eggventure.model.run.RunRepositoryImpl
import com.example.eggventure.utils.sensorutils.EnvironmentSensorManager
import com.example.eggventure.utils.sensorutils.EnvironmentSensorManagerImpl
import com.example.eggventure.utils.sensorutils.StepSensorManager
import com.example.eggventure.utils.sensorutils.StepSensorManagerImpl
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogic
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogicInterface
import com.example.eggventure.viewmodel.creaturelogic.EggHatchEvent
import com.example.eggventure.viewmodel.stats.StatsViewModel
import com.example.eggventure.viewmodel.stepcounter.RunPersistence
import com.example.eggventure.viewmodel.stepcounter.StepCounter
import com.example.eggventure.viewmodel.stepcounter.StepCounterInterface

/**
 * DependencyProvider is a singleton object that provides instances of various repositories and services
 * required by the application. It ensures that all dependencies are created and managed in a single place,
 * promoting better maintainability and testability.
 */
object DependencyProvider {

    private var db: AppDatabase? = null

    private fun getDatabase(context: Context): AppDatabase {
        if (db == null) {
            db = AppDatabase.getDatabase(context)
        }
        return db!!
    }

    fun provideCreatureRepository(context: Context): CreatureRepository {
        return CreatureRepositoryImpl(getDatabase(context).creatureDao())
    }

    fun provideHatchProgressRepository(context: Context): HatchProgressRepository {
        return HatchProgressRepositoryImpl(getDatabase(context).hatchProgressDao())
    }

    fun provideRunRepository(context: Context): RunRepository {
        return RunRepositoryImpl(getDatabase(context).runDao())
    }

    fun provideCreatureData(): CreatureDataInterface {
        return CreatureDatabase
    }

    fun provideEggHatchEvent(context: Context): EggHatchEvent {
        return EggHatchEvent(
            provideHatchProgressRepository(context),
            provideCreatureRepository(context)
        )
    }

    fun provideRunPersistence(context: Context): RunPersistence {
        return RunPersistence(provideRunRepository(context))
    }

    fun provideStepSensorManager(context: Context): StepSensorManager {
        return StepSensorManagerImpl(context)
    }

    fun provideEnvironmentSensorManager(context: Context): EnvironmentSensorManager {
        return EnvironmentSensorManagerImpl(context)
    }

    fun provideStepCounter(context: Context): StepCounterInterface {
        return StepCounter(
            provideStepSensorManager(context),
            provideRunPersistence(context),
            provideHatchProgressRepository(context),
            provideCreatureLogic(context),
            provideEnvironmentSensorManager(context)
        )
    }

    fun provideCreatureLogic(context: Context): CreatureLogicInterface {
        return CreatureLogic(
            provideCreatureData(),
            provideEggHatchEvent(context),
            provideCreatureRepository(context),
            provideEnvironmentSensorManager(context)
        )
    }

    fun provideStats(context: Context): StatsViewModel { //TODO MOVE TO INTERFACE ::)
        return StatsViewModel(provideRunRepository(context))
    }
}
