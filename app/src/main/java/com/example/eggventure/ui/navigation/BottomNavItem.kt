package com.example.eggventure.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Lauf : BottomNavItem("lauf", "Lauf", Icons.Outlined.LocationOn)
    object Sammlung : BottomNavItem("sammlung", "Sammlung", Icons.Outlined.Star)
    object Stats : BottomNavItem("stats", "Stats", Icons.Filled.List)
}
