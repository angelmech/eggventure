package com.example.eggventure.viewmodel

import com.example.eggventure.model.run.RunEntity
import com.example.eggventure.model.run.RunRepository
import com.example.eggventure.model.run.RunRepositoryImpl

class RunPersistence(
    private val runRepository: RunRepository
) {
    suspend fun saveRun(steps: Int) {
        val run = RunEntity(
            steps = steps,
            duration = 0L,
            date = System.currentTimeMillis(),
            averageSpeed = null,
            distanceMeters = null
        )
        runRepository.insertRun(run)
    }
}