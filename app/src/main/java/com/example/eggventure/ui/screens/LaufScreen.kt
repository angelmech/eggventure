package com.example.eggventure.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke // For BorderStroke
import androidx.compose.ui.unit.dp // For dp unit
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
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eggventure.R
import com.example.eggventure.ui.components.TopBar
import com.example.eggventure.utils.permissionutils.PermissionHandler
import com.example.eggventure.utils.permissionutils.PermissionHandlerImpl
import com.example.eggventure.viewmodel.stepcounter.StepCounter
import com.example.eggventure.viewmodel.stepcounter.StepCounterFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaufScreen(
    navController: NavHostController,
) {

    val context = LocalContext.current
    val stepCounter: StepCounter = viewModel(factory = StepCounterFactory(context))

    //-------Observing LiveData-------
    val steps by stepCounter.stepCount.observeAsState(initial = 0)
    val isTracking by stepCounter.isTracking.observeAsState(false)
    val eggHatched by stepCounter.eggHatched.observeAsState()
    val lux = stepCounter.currentLightLevel.collectAsState()

    // Manual hatchGoal because you store it as a var, not LiveData
    val stepGoal = 5000 // or refactor ViewModel to expose this via LiveData if needed
    val progress = steps / stepGoal.toFloat()


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
    val permissionHandler: PermissionHandler = remember { PermissionHandlerImpl(context, permissionLauncher) }
    //-------------------------------------

    // on screen load
    LaunchedEffect(Unit) {
        stepCounter.initProgress()
    }

    Scaffold(
        topBar = { TopBar(title = "Lauf") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                // maybe info button that opens a dialog with hatch info

                /*Icon(
                    Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.Black,)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Sympathy is a Knife von Charlie xcx",
                    fontWeight = FontWeight.Medium,
                    color=Color.Black)*/
            }

            //lux text
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = "Light sensor",
                    tint = Color(0xFFFFA000),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${lux.value?.toInt() ?: "?"}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "Kreaturarten basieren auf Lichtstärke",
                fontSize = 10.sp,
                color = Color.Gray,
                lineHeight = 14.sp,
            )


            // Circular Progress Bar with Egg
            Box(contentAlignment = Alignment.Center) {
                // Background thin gray circle
                CircularProgressIndicator(
                    progress = 1f, // full circle
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(240.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Foreground thick colored progress
                CircularProgressIndicator(
                    progress = progress,
                    strokeWidth = 8.dp,
                    modifier = Modifier.size(240.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                // Center image
                Image(
                    painter = painterResource(id = R.drawable.egg1),
                    contentDescription = "Egg",
                    modifier = Modifier.size(96.dp)
                )
            }



            // Step Count
            Text(
                "$steps / $stepGoal Schritten",
                fontSize = 16.sp,
                color=MaterialTheme.colorScheme.onSurface)

            // Start Button
            Button(
                onClick = {
                    Log.d("LaufScreen", "Button clicked, requesting permission")
                    permissionHandler.checkAndRequestPermission { granted ->
                        if (granted) {
                            if (isTracking) {
                                Log.d("LaufScreen", "Stopping step tracking")
                                stepCounter.stopTracking()
                                Toast.makeText(
                                    context,
                                    "Schrittzähler gestoppt",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Log.d("LaufScreen", "Starting step tracking")
                                stepCounter.startTracking()
                                Toast.makeText(
                                    context,
                                    "Schrittzähler gestartet",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(context, "Berechtigung benötigt", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.size(200.dp, 60.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
            ) {
                Text(
                    text = if (isTracking) "Stopp" else "Lauf Starten",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold ,
                    fontSize = 18.sp,
                )
            }

            Button(
                onClick = {
                    // The onClick logic only triggers if the button is enabled anyway
                    stepCounter.addFakeStep()
                    Log.d("LaufScreen", "Fake step added")
                },
                enabled = isTracking, // Button is enabled (clickable) only when isTracking is true
                modifier = Modifier
                    .alpha(if (isTracking) 1f else 0f)
            ) {
                Text(
                    text = "Schritte simulieren",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )

            }

            eggHatched?.let {
                if (it) {
                    Toast.makeText(context, "Ei geschlüpft!", Toast.LENGTH_SHORT).show()
                    // show screen with hatched creature and "fertig" button
                }
            }

        }

    }

}