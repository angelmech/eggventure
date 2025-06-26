package com.example.eggventure.integration

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eggventure.model.creature.*
import com.example.eggventure.utils.sensorutils.EnvironmentSensorManager
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogic
import com.example.eggventure.viewmodel.creaturelogic.EggHatchEvent
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.flowOf
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreatureLogicIntegrationTest {

    private lateinit var logic: CreatureLogic
    private val hatchEvent = mockk<EggHatchEvent>()
    private val repo = mockk<CreatureRepository>() // Korrigiert
    private val creatureData = mockk<CreatureDataInterface>()
    private val environmentSensorManager = mockk<EnvironmentSensorManager>()

    @Before
    fun setup() {
        every { repo.getAllCreatures() } returns flowOf(emptyList())

        logic = CreatureLogic(
            creatureData,
            hatchEvent,
            repo,
            environmentSensorManager
        )
    }

    @Test
    fun hatchCreatureReturnsTrueWhenSuccess() = runBlocking {
        val entity = CreatureEntity(
            id = 1,
            creatureName = "The World",
            type = CreatureType.REGULAR,
            imageResId = 0,
            rarity = Rarity.RARE,
            hatchedAt = System.currentTimeMillis()
        )

        coEvery {
            hatchEvent.processHatchEvent(
                hatchId = 1,
                currentSteps = 5000,
                goal = 5000,
                hatchTimestamp = any(),
                creatureData = creatureData,
                lightLevel = 100f
            )
        } returns entity

        val result = logic.hatchCreature(
            creatureId = 1,
            totalSteps = 5000,
            hatchProgressSteps = 5000,
            hatchGoal = 5000,
            light = 100f
        )

        Assert.assertTrue(result)

        coVerify {
            hatchEvent.processHatchEvent(
                hatchId = 1,
                currentSteps = 5000,
                goal = 5000,
                hatchTimestamp = any(),
                creatureData = creatureData,
                lightLevel = 100f
            )
        }
    }

    @Test
    fun hatchCreatureReturnsFalseWhenNoCreature() = runBlocking {
        coEvery {
            hatchEvent.processHatchEvent(
                hatchId = 1,
                currentSteps = 1000,
                goal = 5000,
                hatchTimestamp = any(),
                creatureData = creatureData,
                lightLevel = null
            )
        } returns null

        val result = logic.hatchCreature(
            creatureId = 1,
            totalSteps = 1000,
            hatchProgressSteps = 1000,
            hatchGoal = 5000,
            light = null
        )

        Assert.assertFalse(result)

        coVerify {
            hatchEvent.processHatchEvent(
                hatchId = 1,
                currentSteps = 1000,
                goal = 5000,
                hatchTimestamp = any(),
                creatureData = creatureData,
                lightLevel = null
            )
        }
    }
}