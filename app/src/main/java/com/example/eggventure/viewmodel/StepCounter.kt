package com.example.eggventure.viewmodel

import androidx.lifecycle.LiveData

/**
 * Interface for StepCounter functionality.
 */
interface StepCounter {
    /**
     * The number of steps taken.
     */
    val stepCount: LiveData<Int>

    /**
     * Indicates whether the step tracking is currently active.
     */
    val isTracking: LiveData<Boolean>

    /**
     * Indicates whether the egg has hatched.
     */
    val eggHatched: LiveData<Boolean>

    /**
     * Initializes the step tracking progress.
     */
    fun initProgress()

    /**
     * Initializes the step tracking progress.
     */
    fun startTracking()

    /**
     * Stops the step tracking.
     */
    fun stopTracking()

    /**
     * Adds a fake step count for testing purposes.
     *
     * @param fakeStepCount The number of fake steps to add.
     */
    fun addFakeStep(fakeStepCount: Int = 123)
}