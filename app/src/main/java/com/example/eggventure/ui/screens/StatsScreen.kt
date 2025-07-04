package com.example.eggventure.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eggventure.ui.components.TopBar
import com.example.eggventure.viewmodel.stats.Stats
import com.example.eggventure.viewmodel.stats.StatsFactory
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eggventure.model.run.RunEntity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.launch

//MPAndroidChart
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val stats: Stats = viewModel(factory = StatsFactory(context))
    val allRuns = stats.allRuns.collectAsState()

    val listState = rememberLazyListState()
    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset > 200 || listState.firstVisibleItemIndex > 0
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBar(title = "Stats") }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                if (allRuns.value.isNotEmpty()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        item {
                            Text(
                                text = "Deine letzten Läufe",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }

                        item {
                            LastSevenRunsChart(
                                last7Runs = stats.last7Runs.collectAsState().value,
                                statsViewModel = stats
                            )
                        }

                        item {
                            Text(
                                text = "Alle Läufe",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }

                        items(allRuns.value) { run ->
                            RunListItem(
                                run = run,
                                statsViewModel = stats
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Deine Läufe werden hier angezeigt",
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = showScrollToTop,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Nach oben")
                }
            }

        }
    }
}

@Composable
fun LastSevenRunsChart(last7Runs: List<RunEntity>, statsViewModel: Stats) {
    if (last7Runs.isNotEmpty()) {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
        val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()

        last7Runs.forEachIndexed { index, run ->
            entries.add(BarEntry(index.toFloat(), run.steps.toFloat()))
            labels.add(statsViewModel.formatDate(run.date, true))
        }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(vertical = 8.dp),
            factory = { context ->
                BarChart(context).apply {
                    // chart setup
                    description.isEnabled = false
                    legend.isEnabled = false
                    setFitBars(true)
                    setTouchEnabled(false)

                    // X-Axis setup
                    xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(labels) // Set custom labels
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false) // No vertical grid lines
                        setDrawAxisLine(true) // Draw X-axis line
                        granularity = 1f // Minimum interval between values
                        textColor = onSurfaceColor
                        textSize = 10f
                    }

                    // Left Y-Axis setup
                    axisLeft.apply {
                        axisMinimum = 0f // Start from 0
                        setDrawGridLines(true) // Draw horizontal grid lines
                        setDrawAxisLine(true) // Draw Y-axis line
                        textColor = onSurfaceColor
                        textSize = 10f

                    }

                    axisRight.isEnabled = false
                }
            },
            update = { chart ->
                // update when data changes
                val dataSet = BarDataSet(entries, "Steps").apply {
                    color = primaryColor
                    valueTextSize = 12f // Size of value text above bars
                    valueTextColor = onSurfaceColor
                }

                val barData = BarData(dataSet)
                barData.barWidth = 0.5f
                chart.data = barData
                chart.invalidate() // Redraw the chart
            }
        )
    }
}

@Composable
fun RunListItem(run: RunEntity, statsViewModel: Stats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        //elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ){
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Lauf ${run.id}", fontWeight = FontWeight.Bold)
            Text(text = "Datum: ${statsViewModel.formatDate(run.date)}")
            Text(text = "Schritte: ${run.steps}")
            Text(text = "Dauer: ${statsViewModel.formatDuration(run.duration)}")
        }
    }
}

