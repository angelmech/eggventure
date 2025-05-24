package com.example.eggventure.viewmodel.stepcounter

import androidx.lifecycle.LiveData

/**
 * Interface for managing step counting and tracking egg hatching progress.
 */
interface StepCounterInterface {
    /**
     * The current step count.
     * @return A LiveData object that holds the current step count as an Integer.
     */
    val stepCount: LiveData<Int>

    /**
     * Indicates whether the step tracking is currently active.
     * @return A LiveData object that holds a Boolean value; true if tracking is active, false otherwise.
     */
    val isTracking: LiveData<Boolean>

    /**
     * Indicates whether the egg has hatched.
     * @return A LiveData object that holds a Boolean value; true if the egg has hatched, false otherwise.
     */
    val eggHatched: LiveData<Boolean>

    /**
     * Initializes the progress, loading any previously saved state.
     */
    fun initProgress()

    /**
     * Starts the step tracking service.
     */
    fun startTracking()

    /**
     * Stops the step tracking service.
     */
    fun stopTracking()

    /**
     * ONLY FOR PROGRESS TESTING PURPOSES
     * Adds a specified number of fake steps to the current step count.
     * @param fakeSteps The number of fake steps to add.
     */
    fun addFakeStep(fakeSteps: Int = 523)
}