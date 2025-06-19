package com.example.eggventure.viewmodel.creaturelogic

import com.example.eggventure.model.creature.CreatureEntity
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class CreatureSortMode {
    DEFAULT, // by date
    BY_RARITY,
    // BY_NAME

}

class CreatureSortManager {
    private val _sortMode = MutableStateFlow(CreatureSortMode.DEFAULT)
    val sortMode: StateFlow<CreatureSortMode> = _sortMode.asStateFlow()

    fun toggleRaritySort() {
        _sortMode.value = if (_sortMode.value == CreatureSortMode.BY_RARITY) {
            CreatureSortMode.DEFAULT
        } else {
            CreatureSortMode.BY_RARITY
        }
    }

    fun resetSort() {
        _sortMode.value = CreatureSortMode.DEFAULT
    }

    fun sort(creatures: List<CreatureEntity>): List<CreatureEntity> {
        return when (_sortMode.value) {
            CreatureSortMode.BY_RARITY -> creatures.sortedBy { it.rarity }
            CreatureSortMode.DEFAULT -> creatures
        }
    }
}

