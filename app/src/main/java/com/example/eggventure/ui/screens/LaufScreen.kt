package com.example.eggventure.ui.screens

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.provider.FontsContractCompat.Columns
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eggventure.R
import com.example.eggventure.ui.components.TopBar
import com.example.eggventure.utils.PermissionHandler
import com.example.eggventure.viewmodel.StepCounterViewModel
import com.example.eggventure.viewmodel.StepCounterViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaufScreen(
    navController: NavHostController,
    stepGoal: Int = 5000,
) {
    val context = LocalContext.current

    val stepCounterViewModel: StepCounterViewModel = viewModel(
        factory = StepCounterViewModelFactory(context))
    val steps = stepCounterViewModel.stepCount.observeAsState(initial = 0)
    val progress = steps.value / stepGoal.toFloat()
    val isTracking by stepCounterViewModel.isTracking.observeAsState(false)

    //-------Permission Handling-------
    var onPermissionGranted by remember { mutableStateOf<() -> Unit>({}) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Berechtigung erteilt! :)", Toast.LENGTH_SHORT).show()
                onPermissionGranted()
            } else {
                Toast.makeText(context, "Berechtigung verweigert! :(", Toast.LENGTH_SHORT).show()
            }
        }
    )
    val permissionHandler = remember { PermissionHandler(context, permissionLauncher) }
    //-------------------------------------


    Scaffold(
        topBar = { TopBar(title = "Lauf") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F0FB))
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp
                ),
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
            Text("${steps.value} / $stepGoal Schritten", fontSize = 16.sp)

            // Start Button
            Button(
                onClick = {
                    Log.d("LaufScreen", "Button clicked, requesting permission")
                    permissionHandler.checkAndRequestPermission { granted ->
                        if (granted) {
                            if (isTracking) {
                                Log.d("LaufScreen", "Stopping step tracking")
                                stepCounterViewModel.stopStepTracking()
                                Toast.makeText(context, "Schrittzähler gestoppt", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.d("LaufScreen", "Starting step tracking")
                                stepCounterViewModel.startStepTracking()
                                Toast.makeText(context, "Schrittzähler gestartet", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Berechtigung benötigt", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B61FF))
            ) {
                Text(
                    text = if (isTracking) "Stopp" else "Lauf Starten",
                    color = Color.White
                )
            }

            // ✅ Show only while tracking is active
            if (isTracking) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        stepCounterViewModel.addFakeStep()
                    }
                ) {
                    Text("Falschen Schritt hinzufügen")
                }
            }


        }
    }

}