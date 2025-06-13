package com.example.eggventure.viewmodel.creaturelogic

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.viewmodel.DependencyProvider

/**
 * Factory class for creating instances of [CreatureLogic].
 */
class CreatureLogicFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of [CreatureLogic] with the required dependencies.
     *
     * @return An instance of [CreatureLogic] with all dependencies injected.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DependencyProvider.provideCreatureLogic(context) as T
    }
}
