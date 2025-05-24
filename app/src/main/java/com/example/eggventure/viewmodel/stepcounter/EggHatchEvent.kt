package com.example.eggventure.viewmodel.stepcounter

import com.example.eggventure.model.hatchprogress.HatchProgressRepository

class EggHatchEvent(
    private val hatchProgressRepository: HatchProgressRepository
) {
    suspend fun processHatch(hatchId: Int, currentSteps: Int, goal: Int): Boolean {
        return if (currentSteps >= goal) {
            hatchProgressRepository.updateHatchProgress(hatchId, 0)
            true
        } else {
            hatchProgressRepository.updateHatchProgress(hatchId, currentSteps)
            false
        }
    }
}