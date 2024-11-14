package com.cristiangoncas.greenhousemonitor.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cristiangoncas.greenhousemonitor.BuildConfig
import com.cristiangoncas.greenhousemonitor.domain.client.ApiClient
import com.cristiangoncas.greenhousemonitor.domain.data.local.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.domain.data.repository.GreenhouseRepository
import com.cristiangoncas.greenhousemonitor.domain.data.repository.GreenhouseRepositoryImpl
import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartBeatScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeViewModel

@Composable
fun Navigation() {
    val context: Context = LocalContext.current.applicationContext
    val navController = rememberNavController()
    val logRepository: GreenhouseRepository = GreenhouseRepositoryImpl(
        api = ApiClient(apiUrl = BuildConfig.API_IP),
        db = GreenhouseDB.getInstance(context)
    )

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel {
                    HomeViewModel(logRepository)
                },
                onHeartBeatClicked = { navController.navigate(Screen.HeartBeat.route) }
            )
        }
        composable(route = Screen.HeartBeat.route) {
            HeartBeatScreen(
                viewModel = viewModel(),
                onBack = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object HeartBeat : Screen("heartbeat")
}