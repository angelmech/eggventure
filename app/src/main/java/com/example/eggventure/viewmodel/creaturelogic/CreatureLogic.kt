package com.example.eggventure.viewmodel.creaturelogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eggventure.model.creature.Creature
import com.example.eggventure.model.creature.CreatureDataInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreatureLogic(
    private val creature: CreatureDataInterface,
    private val eggHatchEvent: EggHatchEvent
) : ViewModel(), CreatureLogicInterface {

    private val _creatures = MutableStateFlow(creature.getAllCreatures())
    val creatures: StateFlow<List<Creature>> = _creatures.asStateFlow()

    private val _isSortedByRarity = MutableStateFlow(false)
    val isSortedByRarity: StateFlow<Boolean> = _isSortedByRarity.asStateFlow()

    fun getAllCreatures(): List<Creature> {
        return _creatures.value
    }

    // hatch method which calls processHatchEvent in EggHatchEvent
    // fun hatchCreature(creatureId: Int, hatchProgressSteps: Int, hatchGoal: Int): Boolean fomr interface implement here pls

    override suspend fun hatchCreature(
        creatureId: Int,
        totalSteps: Int,
        hatchProgressSteps: Int,
        hatchGoal: Int
    ): Boolean {
        val timestamp = System.currentTimeMillis()

        // Rufe die suspend-Methode korrekt aus einem Coroutine-Kontext auf
        viewModelScope.launch {
            val result = eggHatchEvent.processHatchEvent(
                hatchId = creatureId,
                currentSteps = hatchProgressSteps,
                goal = hatchGoal,
                hatchTimestamp = timestamp,
                creatureData = creature // <== übergib CreatureDataInterface hier
            )

            if (result != null) {
                // Ein CreatureEntity wurde erstellt => Hatch war erfolgreich
                _creatures.value = creature.getAllCreatures() // aktualisiere UI-Liste
            }
        }

        // Rückgabewert sofort false, da das Ergebnis asynchron kommt
        // Falls du wissen musst, ob das Hatch erfolgreich war, verwende einen Callback oder LiveData-Event
        return false
    }
















    //TODO SORTING STUFF CAN RELOCATE TO A DIFFERENT CLASS, and call it from here

    override fun toggleSortByRarity() {
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

}
