package com.example.eggventure.viewmodel.creaturelogic

import androidx.lifecycle.ViewModel
import com.example.eggventure.model.creature.Creature
import com.example.eggventure.model.creature.CreatureDataInterface

class CreatureLogic(private val creature: CreatureDataInterface) : ViewModel() {

    val creatures = creature.getAllCreatures()
}
