package com.example.eggventure.instrumented

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eggventure.viewmodel.stepcounter.StepCounter
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogicInterface
import com.example.eggventure.viewmodel.stepcounter.RunPersistence
import com.example.eggventure.utils.sensorutils.EnvironmentSensorManager
import com.example.eggventure.utils.sensorutils.StepSensorManager
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith

class StepCounterInstrumentedTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: StepCounter
    private lateinit var sensorManager: SensorManager
    private val stepSensorManager = mockk<StepSensorManager>(relaxed = true)
    private val environmentSensorManager = mockk<EnvironmentSensorManager>(relaxed = true)
    private val runPersistence = mockk<RunPersistence>(relaxed = true)
    private val hatchProgressRepository = mockk<HatchProgressRepository>(relaxed = true)
    private val creatureLogic = mockk<CreatureLogicInterface>(relaxed = true)


    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

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
    fun testStepCountingAndHatchingOnDevice() = runTest {
        coEvery { creatureLogic.hatchCreature(any(), any(), any(), any(), any()) } returns true

        val observer = mockk<Observer<Int>>(relaxed = true)
        viewModel.stepCount.observeForever(observer)

        repeat(5000) { viewModel.addFakeStep(1) }

        // Weniger strikte Prüfung
        coVerify(exactly = 1) {
            creatureLogic.hatchCreature(any(), any(), any(), eq(5000), any())
        }

        Assert.assertEquals(0, viewModel.stepCount.value)
    }


    @Test
    fun testStepCountIncreasesCorrectly() = runTest {
        val observer = mockk<Observer<Int>>(relaxed = true)
        viewModel.stepCount.observeForever(observer)

        viewModel.addFakeStep(3)
        viewModel.addFakeStep(2)

        verify { observer.onChanged(5) }
        Assert.assertEquals(5, viewModel.stepCount.value)
    }

    @Test
    fun simulateStepSensorTrigger_updatesStepCount() = runTest {
        val observer = mockk<Observer<Int>>(relaxed = true)
        viewModel.stepCount.observeForever(observer)
        viewModel.addFakeStep(1)
        viewModel.addFakeStep(2)
        verify { observer.onChanged(3) }
        Assert.assertEquals(3, viewModel.stepCount.value)
    }


    @Test
    fun simulateLightSensorInput_affectsHatch() = runTest {
        every { environmentSensorManager.observeLight() } returns flowOf(420.0f)
        coEvery { creatureLogic.hatchCreature(any(), any(), any(), any(), 420.0f) } returns true
        viewModel = StepCounter(
            stepSensorManager,
            runPersistence,
            hatchProgressRepository,
            creatureLogic,
            environmentSensorManager
        )
        viewModel.initProgress()
        repeat(5000) { viewModel.addFakeStep(1) }
        coVerify { creatureLogic.hatchCreature(any(), any(), any(), any(), 420.0f) }
    }
}