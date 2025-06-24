package com.example.eggventure.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eggventure.model.creature.CreatureEntity
import com.example.eggventure.ui.components.CreatureCard
import com.example.eggventure.ui.components.TopBar
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogic
import com.example.eggventure.viewmodel.creaturelogic.CreatureLogicFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SammlungScreen(navController: NavHostController) {

    val context = LocalContext.current
    val creatureLogic: CreatureLogic = viewModel(factory = CreatureLogicFactory(context))
    val creatures by creatureLogic.creatures.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val selectedCreature = remember { mutableStateOf<CreatureEntity?>(null) }


    Scaffold(
        topBar = { TopBar(
            title = "Sammlung",
        ) },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Seltenheit",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            creatureLogic.toggleSortByRarity()
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Datum",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            creatureLogic.resetSort()
                            expanded = false
                        }
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
                FloatingActionButton(
                    onClick = { expanded = !expanded },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.Close else Icons.Filled.Sort,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(creatures) { creature ->
                Box(
                    modifier = Modifier.clickable {
                        selectedCreature.value = creature
                    }
                ) {
                    CreatureCard(creature = creature)
                }
                if (selectedCreature.value != null) {
                    Dialog(onDismissRequest = { selectedCreature.value = null }) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(24.dp)
                                    .wrapContentHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CreatureCard(
                                    creature = selectedCreature.value!!,
                                    expanded = true
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { selectedCreature.value = null }) {
                                    Text(
                                        text = "Abbrechen",
                                        color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}