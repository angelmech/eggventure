package com.example.eggventure.viewmodel.creaturelogic

import androidx.lifecycle.ViewModel
import com.example.eggventure.model.creature.Creature
import com.example.eggventure.model.creature.CreatureDataInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreatureLogic(private val creature: CreatureDataInterface) : ViewModel() {


    private val _creatures = MutableStateFlow(creature.getAllCreatures())
    val creatures: StateFlow<List<Creature>> = _creatures.asStateFlow()

    private val _isSortedByRarity = MutableStateFlow(false)
    val isSortedByRarity: StateFlow<Boolean> = _isSortedByRarity.asStateFlow()

    fun toggleSortByRarity() {
        _isSortedByRarity.value = !_isSortedByRarity.value
        sortCreaturesByRarity(_isSortedByRarity.value)
    }

    private fun sortCreaturesByRarity(sortByRarity: Boolean) {
        _isSortedByRarity.value = !_isSortedByRarity.value
        if (sortByRarity) {
            _creatures.value = _creatures.value.sortedBy { it.rarity }
        } else {
            _creatures.value = creature.getAllCreatures()
        }
    }

    fun resetSort() {
        _isSortedByRarity.value = false
        _creatures.value = creature.getAllCreatures()
    }

    fun getAllCreatures(): List<Creature> {
        return _creatures.value
    }


}
