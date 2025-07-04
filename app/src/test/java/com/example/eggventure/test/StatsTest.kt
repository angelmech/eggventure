package com.example.eggventure.test

import com.example.eggventure.model.run.RunEntity
import com.example.eggventure.model.run.RunRepository
import com.example.eggventure.viewmodel.stats.Stats
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class StatsTest {

    private val runRepository = mockk<RunRepository>()
    private lateinit var viewModel: Stats

    private val testRuns = listOf(
        RunEntity(1, 1000, 2000, 5000, null, null),
        RunEntity(2, 2000, 3000, 8000, null,null)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        coEvery { runRepository.getAllRuns() } returns flowOf(testRuns)
        coEvery { runRepository.getLastRun() } returns testRuns.last()
        coEvery { runRepository.getLast7Runs() } returns testRuns

        viewModel = Stats(runRepository)
    }

    @Test
    fun formatDuration_returnsCorrectFormat() {
        val result = viewModel.formatDuration(3605000) // 1h 0m 5s
        Assert.assertEquals("01:00:05", result)
    }

    @Test
    fun formatDate_returnsCorrectDateString() {
        val timestamp = 0L
        val result = viewModel.formatDate(timestamp)
        Assert.assertTrue(result.contains("1970"))
    }

    @Test
    fun getAllRuns_emitsCorrectRuns() = runTest {
        Assert.assertEquals(testRuns, viewModel.allRuns.value)
    }

    @Test
    fun getLastRun_emitsCorrectLastRun() = runTest {
        Assert.assertEquals(testRuns.last(), viewModel.lastRun.value)
    }

    @Test
    fun getLast7Runs_emitsCorrectSubset() = runTest {
        Assert.assertEquals(testRuns, viewModel.last7Runs.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
