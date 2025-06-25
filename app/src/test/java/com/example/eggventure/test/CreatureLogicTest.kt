package com.example.eggventure.test

import com.example.eggventure.model.creature.*
import com.example.eggventure.utils.sensorutils.EnvironmentSensorManager
import com.example.eggventure.viewmodel.creaturelogic.*
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*

/**
 * Unit tests for CreatureLogic class.
 *
 * 4/4 passed
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CreatureLogicTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var creatureMock: CreatureDataInterface
    private lateinit var eggHatchEventMock: EggHatchEvent
    private lateinit var creatureRepoMock: CreatureRepository
    private lateinit var environmentSensorManagerMock: EnvironmentSensorManager

    private lateinit var creatureLogic: CreatureLogic

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        creatureMock = mockk()
        eggHatchEventMock = mockk()
        creatureRepoMock = mockk()
        environmentSensorManagerMock = mockk()

        // creatureRepository.getAllCreatures() returns empty flow for simplicity
        every { creatureRepoMock.getAllCreatures() } returns flowOf(emptyList())

        creatureLogic = CreatureLogic(
            creatureMock,
            eggHatchEventMock,
            creatureRepoMock,
            environmentSensorManagerMock
        )
    }

    @Test
    fun `hatchCreature calls processHatchEvent and returns true when hatched`() = runTest {
        coEvery {
            eggHatchEventMock.processHatchEvent(any(), any(), any(), any(), any(), any())
        } returns CreatureEntity(
            id = 1,
            creatureName = "Dragon",
            type = CreatureType.RADIANT,
            imageResId = 0,
            rarity = Rarity.LEGENDARY,
            hatchedAt = 123456L
        )

        val result = creatureLogic.hatchCreature(
            creatureId = 1,
            totalSteps = 5000,
            hatchProgressSteps = 5000,
            hatchGoal = 5000,
            light = 100f
        )

        Assert.assertTrue(result)
        coVerify(exactly = 1) {
            eggHatchEventMock.processHatchEvent(
                hatchId = 1,
                currentSteps = 5000,
                goal = 5000,
                hatchTimestamp = any(),
                creatureData = creatureMock,
                lightLevel = 100f
            )
        }
    }

    @Test
    fun `hatchCreature returns false when no creature hatched`() = runTest {
        coEvery {
            eggHatchEventMock.processHatchEvent(any(), any(), any(), any(), any(), any())
        } returns null

        val result = creatureLogic.hatchCreature(
            creatureId = 1,
            totalSteps = 1000,
            hatchProgressSteps = 1000,
            hatchGoal = 5000,
            light = null
        )

        Assert.assertEquals(false, result)
    }

    @Test
    fun `lastLightLevel is updated correctly`() = runTest {
        // Angenommen, es gibt eine Methode, die den Lichtwert setzt
        val field = creatureLogic.javaClass.getDeclaredField("_lastLightLevel")
        field.isAccessible = true
        val stateFlow = field.get(creatureLogic) as MutableStateFlow<Float?>
        stateFlow.value = 42.0f
        Assert.assertEquals(42.0f, creatureLogic.lastLightLevel.value)
    }

    @Test
    fun `creatures list is initially empty`() = runTest {
        Assert.assertTrue(creatureLogic.creatures.value.isEmpty())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}

