package com.example.eggventure.viewmodel.creaturelogic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eggventure.model.creature.Creature
import com.example.eggventure.model.creature.CreatureDataInterface
import com.example.eggventure.model.creature.CreatureEntity
import com.example.eggventure.model.creature.CreatureRepository
import com.example.eggventure.utils.sensorutils.EnvironmentSensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CreatureLogic(
    private val creature: CreatureDataInterface,
    private val eggHatchEvent: EggHatchEvent,
    private val creatureRepository: CreatureRepository,
    private val environmentSensorManager: EnvironmentSensorManager
) : ViewModel(), CreatureLogicInterface {

    private val sortManager = CreatureSortManager()

    private val rawCreatures = creatureRepository.getAllCreatures()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val creatures = combine(rawCreatures, sortManager.sortMode) { creatures, _ ->
        sortManager.sort(creatures)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    override suspend fun hatchCreature(
        creatureId: Int,
        totalSteps: Int,
        hatchProgressSteps: Int,
        hatchGoal: Int
    ): Boolean {
        val timestamp = System.currentTimeMillis()

        // temp + light
        val (temperature, light) = environmentSensorManager.readOnce()
        val result = eggHatchEvent.processHatchEvent(
            hatchId = creatureId,
            currentSteps = hatchProgressSteps,
            goal = hatchGoal,
            hatchTimestamp = timestamp,
            creatureData = creature,
            //temperature = temperature,
            //light = light
        )

        return result != null
    }

    override fun toggleSortByRarity() {
        sortManager.toggleRaritySort()
    }

    override fun resetSort() {
        sortManager.resetSort()
    }
}
