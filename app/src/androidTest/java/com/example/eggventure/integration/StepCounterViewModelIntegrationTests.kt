package com.example.eggventure.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import com.example.eggventure.utils.sensorutils.EnvironmentSensorManager
import com.example.eggventure.utils.sensorutils.StepSensorManager
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogicInterface
import com.example.eggventure.viewmodel.stepcounter.RunPersistence
import com.example.eggventure.viewmodel.stepcounter.StepCounter
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StepCounterViewModelIntegrationTests {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: StepCounter
    private val stepSensorManager = mockk<StepSensorManager>(relaxed = true)
    private val runPersistence = mockk<RunPersistence>(relaxed = true)
    private val hatchProgressRepository = mockk<HatchProgressRepository>(relaxed = true)
    private val creatureLogic = mockk<CreatureLogicInterface>(relaxed = true)
    private val environmentSensorManager = mockk<EnvironmentSensorManager>(relaxed = true)

    @Before
    fun setup() {
        coEvery { hatchProgressRepository.getLastHatchProgress() } returns null
        viewModel = StepCounter(
            stepSensorManager,
            runPersistence,
            hatchProgressRepository,
            creatureLogic,
            environmentSensorManager
        )
        viewModel.initProgress()
    }

    @Test
    fun stepIncreasesAndTriggersHatchEvent() = runTest {
        val observer = mockk<Observer<Int>>(relaxed = true)
        viewModel.stepCount.observeForever(observer)

        viewModel.addFakeStep(1)
        viewModel.addFakeStep(1)

        Assert.assertEquals(2, viewModel.stepCount.value)
        verify { observer.onChanged(2) }
    }

    @Test
    fun hatchResetsProgressOnSuccess() = runTest {
        coEvery { creatureLogic.hatchCreature(any(), any(), any(), any(), any()) } returns true

        repeat(5000) { viewModel.addFakeStep(1) }

        coVerify { creatureLogic.hatchCreature(any(), any(), any(), any(), any()) }
        Assert.assertEquals(0, viewModel.stepCount.value)
    }
}