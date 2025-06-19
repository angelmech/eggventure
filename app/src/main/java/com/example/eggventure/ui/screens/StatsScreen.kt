package com.example.eggventure.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.eggventure.viewmodel.StatsViewModel
import com.example.eggventure.viewmodel.StatsViewModelFactory
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eggventure.model.run.RunEntity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val stats: StatsViewModel = viewModel(factory = StatsViewModelFactory(context))

    val allRuns = stats.allRuns.collectAsState()

    Scaffold(
        topBar = { TopBar(title = "Stats") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            // ---Hier Seiteninhalt einfügen---

            Text (text = "Total Runs: ${allRuns.value.size}",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,)
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp) // Horizontal padding for list items
            ) {
                items(allRuns.value) {run ->
                    RunListItem(
                        run = run,
                        statsViewModel = stats
                    )
                }
            }

        }
    }
}

@Composable
fun RunListItem(run: RunEntity, statsViewModel: StatsViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

        ){
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Lauf ${run.id}", fontWeight = FontWeight.Bold)
            Text(text = "Datum: ${statsViewModel.formatDate(run.date)}")
            Text(text = "Schritte: ${run.steps}")
            Text(text = "Dauer: ${statsViewModel.formatDuration(run.duration)}")
        }
    }
}
