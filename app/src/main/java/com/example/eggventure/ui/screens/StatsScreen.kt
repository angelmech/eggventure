package com.example.eggventure.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eggventure.ui.components.TopBar
import com.example.eggventure.viewmodel.StatsViewModel
import com.example.eggventure.viewmodel.StatsViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val stats: StatsViewModel = viewModel(factory = StatsViewModelFactory(context))

    Scaffold(
        topBar = { TopBar(title = "Stats") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            // ---Hier Seiteninhalt einfügen---
        }
    }
}