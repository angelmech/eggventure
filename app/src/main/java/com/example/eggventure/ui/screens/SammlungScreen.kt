package com.example.eggventure.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eggventure.R
import com.example.eggventure.ui.components.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SammlungScreen(navController: NavHostController) {

    val eggCreatures = listOf(
        "Egg A", "Egg B", "Egg C", "Egg D", "Egg E", "Egg F"
    ) // !! example data, change later to pics or sum shit

    Scaffold(
        topBar = { TopBar(title = "Sammlung") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(eggCreatures.size) { index ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(Color(0xFFEDE7F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = eggCreatures[index], color = Color.DarkGray) // this da placeholder for now

                        //-----IMPORTANT!!!! FOR LATER USE---!----!---!--!---
                        /*Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Ei",
                            modifier = Modifier.size(64.dp)
                        )*/
                    }
                }
            }
        }
    }
}
