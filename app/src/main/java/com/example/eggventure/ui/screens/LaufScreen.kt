package com.example.eggventure.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.provider.FontsContractCompat.Columns
import androidx.navigation.NavHostController
import com.example.eggventure.R

@Composable
fun LaufScreen(
    navController: NavHostController,
    steps: Int = 2002, // !!Example step count, change later
    stepGoal: Int = 5000,
) {
    val progress = steps / stepGoal.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F0FB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Song Title
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.MusicNote, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Sympathy is a Knife von Charlie xcx", fontWeight = FontWeight.Medium) // !!Example song, change later
        }

        // Circular Progress Bar with Egg
        Box(
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = progress,
                strokeWidth = 8.dp,
                modifier = Modifier.size(240.dp),
                color = Color(0xFF7B61FF)
            )
            Image(
                painter = painterResource(id = R.drawable.egg1), //
                contentDescription = "Egg",
                modifier = Modifier.size(64.dp)
            )
        }

        // Step Count
        Text("$steps / $stepGoal Schritten", fontSize = 16.sp)

        // Start Button
        Button(
            onClick = {}, // !! startStepCount() should be called here
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B61FF))
        ) {
            Text("Lauf Starten", color = Color.White)
        }


    }

}