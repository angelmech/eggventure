package com.example.eggventure.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.eggventure.ui.components.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavHostController) {
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