package com.cristiangoncas.greenhousemonitor.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartBeatScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.logs.LogsScreen

@Composable
fun NavigationGraph(navHostController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navHostController,
        startDestination = BottomNavItem.Home.route,
    ) {
        composable(route = BottomNavItem.Home.route) {
            HomeScreen()
        }
        composable(route = BottomNavItem.Logs.route) {
            LogsScreen(
                innerPadding = innerPadding
            )
        }
        composable(route = BottomNavItem.Heartbeat.route) {
            HeartBeatScreen(
                innerPadding = innerPadding
            )
        }
    }
}
