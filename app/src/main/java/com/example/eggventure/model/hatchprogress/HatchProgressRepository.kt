package com.example.eggventure.model.hatchprogress

interface HatchProgressRepository {
    suspend fun insertProgress(progress: HatchProgressEntity)
    suspend fun getHatchProgressSteps(): Int?
    suspend fun getHatchGoal(): Int?
    suspend fun getLastHatchProgress(): HatchProgressEntity?
    suspend fun updateHatchProgress(id: Int, stepsAccumulated: Int)
}
