package com.example.eggventure.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF7B61FF),
            titleContentColor = Color.White
        )

    )
}
