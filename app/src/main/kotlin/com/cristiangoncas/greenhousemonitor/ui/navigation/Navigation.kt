package com.cristiangoncas.greenhousemonitor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartBeatScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable(route = "home") {
            HomeScreen(
                viewModel = viewModel(),
                onHeartBeatClicked = { navController.navigate("heartbeat") }
            )
        }
        composable(route = "heartbeat") {
            HeartBeatScreen(
                viewModel = viewModel(),
                onBack = { navController.popBackStack() }
            )
        }
    }
}