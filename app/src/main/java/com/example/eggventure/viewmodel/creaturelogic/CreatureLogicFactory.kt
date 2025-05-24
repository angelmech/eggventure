package com.example.eggventure.viewmodel.creaturelogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.model.creature.CreatureDataInterface

class CreatureViewModelFactory(
    private val creatureSource: CreatureDataInterface
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")

        return CreatureLogic(creatureSource) as T
    }
}
