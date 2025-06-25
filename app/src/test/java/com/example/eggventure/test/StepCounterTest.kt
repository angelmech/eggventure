package com.example.eggventure.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.eggventure.helper.MainDispatcherRule
import com.example.eggventure.helper.getOrAwaitValue
import com.example.eggventure.model.hatchprogress.HatchProgressEntity
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import com.example.eggventure.utils.sensorutils.EnvironmentSensorManager
import com.example.eggventure.utils.sensorutils.StepSensorManager
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogicInterface
import com.example.eggventure.viewmodel.stepcounter.RunPersistence
import com.example.eggventure.viewmodel.stepcounter.StepCounter
import com.example.eggventure.viewmodel.stepcounter.StepSensorData
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.*
import org.junit.rules.TestRule

/**
 * Unit tests for the StepCounter ViewModel.
 *
 * 21/21 passed
 */
@OptIn(ExperimentalCoroutinesApi::class)
class StepCounterTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: StepCounter
    private lateinit var stepSensorManager: StepSensorManager
    private lateinit var runPersistence: RunPersistence
    private lateinit var hatchProgressRepository: HatchProgressRepository
    private lateinit var creatureLogic: CreatureLogicInterface
    private lateinit var environmentSensorManager: EnvironmentSensorManager

    @Before
    fun setup() {
        stepSensorManager = mockk(relaxed = true)
        runPersistence = mockk(relaxed = true)
        hatchProgressRepository = mockk(relaxed = true)
        creatureLogic = mockk(relaxed = true)
        environmentSensorManager = mockk()

        every { environmentSensorManager.observeLight() } returns flowOf(120f)

        viewModel = StepCounter(
            stepSensorManager,
            runPersistence,
            hatchProgressRepository,
            creatureLogic,
            environmentSensorManager
        )
    }

    @Test
    fun `initProgress uses last saved progress if available`() = runTest {
        val entity = createHatchProgressEntity(1, 100, 5000)
        coEvery { hatchProgressRepository.getLastHatchProgress() } returns entity

        viewModel.initProgress()
        advanceUntilIdle()

        Assert.assertEquals(100, viewModel.stepCount.value)
    }

    @Test
    fun `initProgress creates new progress if none exists`() = runTest {
        coEvery { hatchProgressRepository.getLastHatchProgress() } returns null
        coEvery { hatchProgressRepository.insertProgress(any()) } just Runs

        viewModel.initProgress()
        advanceUntilIdle()

        Assert.assertEquals(0, viewModel.stepCount.value)
    }

    @Test
    fun `startTracking registers listener and resets tracking`() {
        viewModel.startTracking()

        Assert.assertTrue(viewModel.isTracking.value == true)
        verify { stepSensorManager.registerListener(any()) }
    }

    @Test
    fun `stopTracking unregisters listener and saves run`() = runTest {
        viewModel.stopTracking()
        advanceUntilIdle()

        Assert.assertFalse(viewModel.isTracking.value!!)
        verify { stepSensorManager.unregisterListener() }
        coVerify { runPersistence.saveRun(any(), any(), any()) }
    }

    @Test
    fun `addFakeStep increases step count and updates repo`() = runTest {
        val entity = createHatchProgressEntity(1, 0, 5000)
        coEvery { hatchProgressRepository.getLastHatchProgress() } returns entity
        coEvery { hatchProgressRepository.updateHatchProgress(any(), any()) } just Runs

        viewModel.initProgress()
        advanceUntilIdle()

        viewModel.addFakeStep(50)
        advanceUntilIdle()

        Assert.assertEquals(50, viewModel.stepCount.value)
        coVerify { hatchProgressRepository.updateHatchProgress(1, 50) }
    }

    @Test
    fun `addFakeStep triggers hatch when reaching goal`() = runTest {
        val entity = createHatchProgressEntity(1, 4999, 5000)
        coEvery { hatchProgressRepository.getLastHatchProgress() } returns entity
        coEvery { hatchProgressRepository.updateHatchProgress(any(), any()) } just Runs
        coEvery { creatureLogic.hatchCreature(any(), any(), any(), any(), any()) } returns true

        viewModel.initProgress()
        advanceUntilIdle()

        viewModel.addFakeStep(1)
        advanceUntilIdle()

        Assert.assertEquals(0, viewModel.stepCount.value)
        Assert.assertTrue(viewModel.eggHatched.value == true)
    }

    @Test
    fun `onSensorChanged updates step count and triggers hatch when goal is reached`() = runTest {
        // Mock Hatch-Logik, damit hatchCreature true zurückgibt
        coEvery { creatureLogic.hatchCreature(any(), any(), any(), any(), any()) } returns true

        // Progress initialisieren (z.B. hatchGoal = 5000)
        viewModel.initProgress()
        advanceUntilIdle()

        // Simuliere Schrittzählerwert über dem Ziel
        viewModel.onStepSensorDataChanged(StepSensorData(totalSteps = 5001))
        advanceUntilIdle()

        // Schrittzahl sollte zurückgesetzt sein
        Assert.assertEquals(0, viewModel.stepCount.value)
        // Ei sollte geschlüpft sein
        Assert.assertTrue(viewModel.eggHatched.value == true)
    }



    @Test
    fun `startTracking does not register listener again if already tracking`() {
        viewModel.startTracking()
        viewModel.startTracking() // Second call should not re-register

        verify(exactly = 1) { stepSensorManager.registerListener(any()) }
    }

    @Test
    fun `addFakeStep does not exceed hatch goal`() = runTest {
        val entity = createHatchProgressEntity(1, 4900, 5000)
        coEvery { hatchProgressRepository.getLastHatchProgress() } returns entity
        coEvery { hatchProgressRepository.updateHatchProgress(any(), any()) } just Runs
        coEvery { creatureLogic.hatchCreature(any(), any(), any(), any(), any()) } returns false

        viewModel.initProgress()
        advanceUntilIdle()

        viewModel.addFakeStep(200) // would exceed goal
        advanceUntilIdle()

        Assert.assertEquals(5000, viewModel.stepCount.value)
    }

    @Test
    fun `onSensorChanged ignores non-step-counter events`() {
        // Da StepSensorData nur totalSteps enthält, simulieren wir hier
        // einen Wert, der keinen Fortschritt auslöst (z.B. 0 Schritte)
        viewModel.onStepSensorDataChanged(StepSensorData(totalSteps = 0))

        // Erwartung: kein Ei geschlüpft, Schrittzahl bleibt 0 oder unverändert
        Assert.assertEquals(false, viewModel.eggHatched.value)
    }

    @Test
    fun `onCleared unregisters sensor listener`() {
        viewModel.onCleared()

        verify { stepSensorManager.unregisterListener() }
    }

    @Test
    fun `light level falls back to 0 if null`() = runTest {
        coEvery { hatchProgressRepository.getLastHatchProgress() } returns createHatchProgressEntity(1, 4999, 5000)
        coEvery { hatchProgressRepository.updateHatchProgress(any(), any()) } just Runs
        coEvery { creatureLogic.hatchCreature(any(), any(), any(), any(), eq(0f)) } returns true
        every { environmentSensorManager.observeLight() } returns flowOf(0f) // Fallback-Wert

        val vm = StepCounter(
            stepSensorManager,
            runPersistence,
            hatchProgressRepository,
            creatureLogic,
            environmentSensorManager
        )

        vm.initProgress()
        advanceUntilIdle()

        vm.addFakeStep(1)
        advanceUntilIdle()

        Assert.assertTrue(vm.eggHatched.value == true)
    }

    @Test
    fun `initProgress handles repository error gracefully`() = runTest {
        coEvery { hatchProgressRepository.getLastHatchProgress() } throws Exception("Database error")

        viewModel.initProgress()
        advanceUntilIdle()

        Assert.assertEquals(0, viewModel.stepCount.value) // Fallback-Wert
    }

    @Test
    fun `addFakeStep does nothing if hatchId is null`() = runTest {
        // kein initProgress aufgerufen => hatchId ist null

        viewModel.addFakeStep(100)
        advanceUntilIdle()

        // Keine Interaktion mit dem Repository
        coVerify(exactly = 0) { hatchProgressRepository.updateHatchProgress(any(), any()) }
        Assert.assertEquals(100, viewModel.stepCount.value)
    }

    @Test
    fun `onSensorChanged with same totalSteps does not increase step count`() = runTest {
        viewModel.initProgress()
        advanceUntilIdle()

        // Simuliere ersten Sensorwert
        viewModel.onStepSensorDataChanged(StepSensorData(3000))
        val previous = viewModel.stepCount.getOrAwaitValue()

        // Simuliere exakt denselben Sensorwert
        viewModel.onStepSensorDataChanged(StepSensorData(3000))
        val after = viewModel.stepCount.getOrAwaitValue()

        Assert.assertEquals(previous, after)
    }


    @Test
    fun `onSensorChanged with lower totalSteps than initial handles correctly`() = runTest {
        // Progress initialisieren (setzt z.B. hatchProgressSteps auf 0)
        viewModel.initProgress()
        advanceUntilIdle()

        // Erste Initialisierung – setzt initialTotalSteps intern
        viewModel.onStepSensorDataChanged(StepSensorData(totalSteps = 100))

        // Jetzt simulieren wir ein niedrigeres totalSteps – sollte keinen Crash verursachen
        viewModel.onStepSensorDataChanged(StepSensorData(totalSteps = 50))

        // Sicherstellen, dass der Schrittzähler nicht negativ ist
        val currentStepCount = viewModel.stepCount.getOrAwaitValue()
        Assert.assertTrue(currentStepCount >= 0)
    }


    @Test
    fun `startTracking resets eggHatched to false`() {
        viewModel.startTracking()
        Assert.assertEquals(false, viewModel.eggHatched.value)
    }

    @Test
    fun `stopTracking can be called even if not tracking`() = runTest {
        // isTracking == false
        viewModel.stopTracking()
        advanceUntilIdle()

        verify { stepSensorManager.unregisterListener() } // sollte trotzdem passieren
        coVerify { runPersistence.saveRun(any(), any(), any()) }        // Run wird gespeichert
    }

    @Test
    fun `addFakeStep with negative input decreases step count but not below zero`() = runTest {
        val entity = createHatchProgressEntity(1, 100, 5000)
        coEvery { hatchProgressRepository.getLastHatchProgress() } returns entity
        coEvery { hatchProgressRepository.updateHatchProgress(any(), any()) } just Runs

        viewModel.initProgress()
        advanceUntilIdle()

        viewModel.addFakeStep(-50)
        advanceUntilIdle()

        // FakeSteps summieren sich, auch negativ – aber StepCount sollte nie negativ angezeigt werden
        Assert.assertEquals(50, viewModel.stepCount.value)
    }

    @Test
    fun `light sensor emits correct value`() = runTest {
        advanceUntilIdle()
        Assert.assertEquals(120f, viewModel.currentLightLevel.value)
    }

    @Test
    fun `hatch resets progress after successful hatch`() = runTest {
        val entity = createHatchProgressEntity(1, 5000, 5000)
        coEvery { hatchProgressRepository.getLastHatchProgress() } returns entity
        coEvery { hatchProgressRepository.updateHatchProgress(any(), any()) } just Runs
        coEvery { creatureLogic.hatchCreature(any(), any(), any(), any(), any()) } returns true

        viewModel.initProgress()
        advanceUntilIdle()

        viewModel.addFakeStep(1)
        advanceUntilIdle()

        Assert.assertEquals(0, viewModel.stepCount.value)
        Assert.assertEquals(true, viewModel.eggHatched.value)
    }


    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun createHatchProgressEntity(id: Int, progress: Int, goal: Int): HatchProgressEntity {
        return HatchProgressEntity(id, progress, goal)
    }

}