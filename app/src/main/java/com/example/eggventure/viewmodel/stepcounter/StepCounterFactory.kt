package com.example.eggventure.viewmodel.stepcounter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.viewmodel.DependencyProvider

/**
 * Factory class for creating instances of [StepCounter].
 */
class StepCounterFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of [StepCounter] with the required dependencies.
     *
     * @return An instance of [StepCounter] with all dependencies injected.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DependencyProvider.provideStepCounter(context) as T
    }
}


