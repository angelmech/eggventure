package com.example.eggventure.test

import com.example.eggventure.model.creature.Creature
import com.example.eggventure.model.creature.CreatureDataInterface
import com.example.eggventure.model.creature.CreatureRepository
import com.example.eggventure.model.creature.CreatureType
import com.example.eggventure.model.creature.Rarity
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import com.example.eggventure.viewmodel.creaturelogic.EggHatchEvent
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for EggHatchEvent class
 *
 * 8/8 passed
 */

@OptIn(ExperimentalCoroutinesApi::class)
class EggHatchEventTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var hatchProgressRepository: HatchProgressRepository
    private lateinit var creatureRepository: CreatureRepository
    private lateinit var eggHatchEvent: EggHatchEvent

    private val sampleCreatures = listOf(
        Creature(1, "Echoes", CreatureType.REGULAR, 0),
        Creature(2, "D4C", CreatureType.SHADOW, 0),
        Creature(3, "SCR", CreatureType.SHADOW, 0),
        Creature(4, "Killer Queen", CreatureType.RADIANT, 0),
        Creature(5, "Star Platinum", CreatureType.RADIANT, 0),
        Creature(6, "GER", CreatureType.REGULAR, 0),
        Creature(7, "Weather Report", CreatureType.RADIANT, 0),
        Creature(8, "The World", CreatureType.REGULAR, 0),
        Creature(9, "Chariot Baby", CreatureType.SHADOW, 0),
        Creature(10, "Heavens Door", CreatureType.RADIANT, 0)
    )

    private val creatureData = object : CreatureDataInterface {
        override fun getAllCreatures(): List<Creature> = sampleCreatures
        override fun getById(id: Int): Creature? = sampleCreatures.find { it.id == id }
        override fun getByName(name: String): Creature? = sampleCreatures.find { it.name == name }
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        hatchProgressRepository = mockk(relaxed = true)
        creatureRepository = mockk(relaxed = true)

        eggHatchEvent = EggHatchEvent(hatchProgressRepository, creatureRepository)
    }

    @Test
    fun `processHatchEvent updates progress and returns null if currentSteps below goal`() = runTest {
        val result = eggHatchEvent.processHatchEvent(
            hatchId = 1,
            currentSteps = 3000,
            goal = 5000,
            creatureData = creatureData,
            lightLevel = 500f
        )
        coVerify(exactly = 1) { hatchProgressRepository.updateHatchProgress(1, 3000) }
        Assert.assertNull(result)
    }

    @Test
    fun `processHatchEvent resets progress, inserts creature and returns entity if goal reached`() = runTest {
        coEvery { creatureRepository.insertCreature(any()) } just Runs

        val result = eggHatchEvent.processHatchEvent(
            hatchId = 1,
            currentSteps = 5000,
            goal = 5000,
            creatureData = creatureData,
            lightLevel = 8000f
        )

        coVerifySequence {
            hatchProgressRepository.updateHatchProgress(1, 0)
            creatureRepository.insertCreature(any())
        }
        Assert.assertNotNull(result)
        // result kann null sein, daher safe call
        Assert.assertTrue(result?.hatchedAt ?: 0 > 0)
        Assert.assertTrue(result != null && sampleCreatures.any { it.name == result.creatureName })
    }

    @Test
    fun `determineType returns SHADOW for light below 700`() = runTest {
        val method = EggHatchEvent::class.java.getDeclaredMethod("determineType", Float::class.javaObjectType)
        method.isAccessible = true
        val type = method.invoke(eggHatchEvent, 500f) as CreatureType
        Assert.assertEquals(CreatureType.SHADOW, type)
    }

    @Test
    fun `determineType returns REGULAR for light between 700 and 10000`() = runTest {
        val method = EggHatchEvent::class.java.getDeclaredMethod("determineType", Float::class.javaObjectType)
        method.isAccessible = true
        val type = method.invoke(eggHatchEvent, 5000f) as CreatureType
        Assert.assertEquals(CreatureType.REGULAR, type)
    }

    @Test
    fun `determineType returns RADIANT for light above 10000`() = runTest {
        val method = EggHatchEvent::class.java.getDeclaredMethod("determineType", Float::class.javaObjectType)
        method.isAccessible = true
        val type = method.invoke(eggHatchEvent, 20000f) as CreatureType
        Assert.assertEquals(CreatureType.RADIANT, type)
    }

    @Test
    fun `pickCreatureBasedOnRarityAndType falls back to any creature if none match type`() = runTest {
        val fallbackCreature = Creature(99, "Fallback", CreatureType.REGULAR, 0)
        val emptyTypeData = object : CreatureDataInterface {
            override fun getAllCreatures(): List<Creature> = listOf(fallbackCreature)
            override fun getById(id: Int): Creature? = null
            override fun getByName(name: String): Creature? = null
        }
        val method = EggHatchEvent::class.java.getDeclaredMethod("pickCreatureBasedOnRarityAndType", CreatureDataInterface::class.java, Float::class.javaObjectType)
        method.isAccessible = true
        val result = method.invoke(eggHatchEvent, emptyTypeData, 500f) as Creature
        Assert.assertNotNull(result)
        Assert.assertEquals(fallbackCreature.name, result.name)
    }

    @Test
    fun `rarityRoll always returns valid Rarity`() = runTest {
        val method = EggHatchEvent::class.java.getDeclaredMethod("rarityRoll")
        method.isAccessible = true
        repeat(100) {
            val rarity = method.invoke(eggHatchEvent) as Rarity
            Assert.assertTrue(Rarity.values().contains(rarity))
        }
    }

    @Test
    fun `processHatchEvent works with null lightLevel`() = runTest {
        coEvery { creatureRepository.insertCreature(any()) } just Runs
        val result = eggHatchEvent.processHatchEvent(
            hatchId = 1,
            currentSteps = 5000,
            goal = 5000,
            creatureData = creatureData,
            lightLevel = null
        )
        Assert.assertNotNull(result)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}