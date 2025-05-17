package com.example.eggventure.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eggventure.ui.screens.StatsScreen
import com.example.eggventure.ui.screens.SammlungScreen
import com.example.eggventure.ui.screens.LaufScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Lauf.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Lauf.route) {
            LaufScreen(navController)
        }
        composable(BottomNavItem.Sammlung.route) {
            SammlungScreen(navController)
        }
        composable(BottomNavItem.Stats.route) {
            StatsScreen(navController)
        }
    }
}
