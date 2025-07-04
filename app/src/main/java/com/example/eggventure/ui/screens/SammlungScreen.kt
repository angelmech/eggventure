package com.example.eggventure.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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
        topBar = {
            TopBar(title = "Sammlung")
        },
        floatingActionButton = {
            FloatingSortMenu(
                expanded = expanded,
                onToggle = { expanded = !expanded },
                onSortByRarity = { creatureLogic.toggleSortByRarity() },
                onSortByType = { creatureLogic.toggleSortByType() },
                onSortByName = { creatureLogic.toggleSortByName() },
                onReset = { creatureLogic.resetSort() }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
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
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Schließen",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SortMenuItem(
    label: String,
    textColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)) // Rounded edges for nice effect
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), // Subtle feedback color
        tonalElevation = 0.dp
    ) {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}



@Composable
fun FloatingSortMenu(
    expanded: Boolean,
    onToggle: () -> Unit,
    onSortByRarity: () -> Unit,
    onSortByType: () -> Unit,
    onSortByName: () -> Unit,
    onReset: () -> Unit
) {
    // Animate width based on expansion state
    val targetWidth = if (expanded) 140.dp else 56.dp
    val cardWidth by animateDpAsState(targetValue = targetWidth, label = "CardWidthAnimation")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 10.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.width(cardWidth)
        ) {
            Box(
                modifier = Modifier.padding(8.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AnimatedVisibility(visible = expanded) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            SortMenuItem("Seltenheit", MaterialTheme.colorScheme.onSurface) {
                                onSortByRarity()
                                onToggle()
                            }
                            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), thickness = 1.dp)

                            SortMenuItem("Typ", MaterialTheme.colorScheme.onSurface) {
                                onSortByType()
                                onToggle()
                            }
                            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), thickness = 1.dp)

                            SortMenuItem("Name", MaterialTheme.colorScheme.onSurface) {
                                onSortByName()
                                onToggle()
                            }
                            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), thickness = 1.dp)

                            SortMenuItem("Datum (default)", MaterialTheme.colorScheme.onSurface) {
                                onReset()
                                onToggle()
                            }
                        }
                    }


                    // Button inside a fixed-size box so it doesn't move
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .align(Alignment.End)
                    ) {
                        FloatingActionButton(
                            onClick = onToggle,
                            containerColor = MaterialTheme.colorScheme.primary,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                focusedElevation = 0.dp,
                            ),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            AnimatedContent(
                                targetState = expanded,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "FABIconChange"
                            ) { isExpanded ->
                                Icon(
                                    imageVector = if (isExpanded) Icons.Filled.Close else Icons.Filled.Sort,
                                    contentDescription = if (isExpanded) "Schließen" else "Sortieren",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}





