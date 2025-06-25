package com.example.eggventure.test

import com.example.eggventure.model.creature.CreatureEntity
import com.example.eggventure.model.creature.CreatureType
import com.example.eggventure.model.creature.Rarity
import com.example.eggventure.viewmodel.creaturelogic.CreatureSortManager
import com.example.eggventure.viewmodel.creaturelogic.CreatureSortMode
import org.junit.Assert
import org.junit.Test

/**
 * Unit tests for CreatureSort
 *
 * 4/4 passed
 */

class CreatureSortTest {

    private val creatures = listOf(
        CreatureEntity(
            id = 1,
            creatureName = "A",
            type = CreatureType.REGULAR,
            imageResId = 0,
            rarity = Rarity.COMMON,
            hatchedAt = 1L
        ),
        CreatureEntity(
            id = 2,
            creatureName = "B",
            type = CreatureType.SHADOW,
            imageResId = 0,
            rarity = Rarity.EPIC,
            hatchedAt = 2L
        ),
        CreatureEntity(
            id = 3,
            creatureName = "C",
            type = CreatureType.RADIANT,
            imageResId = 0,
            rarity = Rarity.LEGENDARY,
            hatchedAt = 3L
        )
    )

    private val sortManager = CreatureSortManager()

    @Test
    fun `default sort mode is DEFAULT and preserves order`() {
        Assert.assertEquals(CreatureSortMode.DEFAULT, sortManager.sortMode.value)
        val sorted = sortManager.sort(creatures)
        Assert.assertEquals(creatures, sorted)
    }

    @Test
    fun `toggleRaritySort toggles between BY_RARITY and DEFAULT`() {
        sortManager.toggleRaritySort()
        Assert.assertEquals(CreatureSortMode.BY_RARITY, sortManager.sortMode.value)
        sortManager.toggleRaritySort()
        Assert.assertEquals(CreatureSortMode.DEFAULT, sortManager.sortMode.value)
    }

    @Test
    fun `sort sorts by rarity when in BY_RARITY mode`() {
        sortManager.toggleRaritySort()
        val sorted = sortManager.sort(creatures)
        Assert.assertEquals(listOf(creatures[0], creatures[1], creatures[2]).sortedBy { it.rarity }, sorted)
    }

    @Test
    fun `resetSort sets mode to DEFAULT`() {
        sortManager.toggleRaritySort()
        sortManager.resetSort()
        Assert.assertEquals(CreatureSortMode.DEFAULT, sortManager.sortMode.value)
    }
}
