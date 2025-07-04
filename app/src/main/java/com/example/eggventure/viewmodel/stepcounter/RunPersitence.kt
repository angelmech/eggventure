package com.example.eggventure.viewmodel.stepcounter

import com.example.eggventure.model.run.RunEntity
import com.example.eggventure.model.run.RunRepository

/**
 * ViewModel for persisting run data.
 *
 * This class is responsible for saving run data such as steps, duration, and date.
 * It interacts with the RunRepository to perform the actual data storage.
 *
 * @property runRepository The repository used to interact with the run data source.
 */
class RunPersistence(
    private val runRepository: RunRepository
) {
    suspend fun saveRun(steps: Int, duration: Long, date: Long) {
        val run = RunEntity(
            steps = steps,
            duration = duration,
            date = date,
            averageSpeed = null,
            distanceMeters = null
        )
        runRepository.insertRun(run)
    }
}