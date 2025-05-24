package com.example.eggventure.viewmodel.creaturelogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.model.creature.CreatureDataInterface
import com.example.eggventure.model.creature.CreatureDatabase

class CreatureLogicFactory(
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val creatureSource: CreatureDataInterface = CreatureDatabase

        @Suppress("UNCHECKED_CAST")
        return CreatureLogic(creatureSource) as T
    }
}
