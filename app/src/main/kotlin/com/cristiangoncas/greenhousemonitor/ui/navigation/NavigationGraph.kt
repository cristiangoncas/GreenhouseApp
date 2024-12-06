package com.cristiangoncas.greenhousemonitor.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cristiangoncas.greenhousemonitor.BuildConfig
import com.cristiangoncas.greenhousemonitor.domain.client.ApiClient
import com.cristiangoncas.greenhousemonitor.domain.data.local.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.domain.data.repository.GreenhouseRepository
import com.cristiangoncas.greenhousemonitor.domain.data.repository.GreenhouseRepositoryImpl
import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartBeatScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartbeatViewModel
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeViewModel
import com.cristiangoncas.greenhousemonitor.ui.screen.logs.LogsScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.logs.LogsViewModel

@Composable
fun NavigationGraph(navHostController: NavHostController, innerPadding: PaddingValues) {
    val context: Context = LocalContext.current.applicationContext

    val logRepository: GreenhouseRepository = GreenhouseRepositoryImpl(
        api = ApiClient(apiUrl = BuildConfig.API_IP),
        db = GreenhouseDB.getInstance(context)
    )

    NavHost(
        navController = navHostController,
        startDestination = BottomNavItem.Home.route,
    ) {
        composable(route = BottomNavItem.Home.route) {
            HomeScreen(
                viewModel = viewModel {
                    HomeViewModel(logRepository)
                },
                innerPadding = innerPadding
            )
        }
        composable(route = BottomNavItem.Logs.route) {
            LogsScreen(
                viewModel = viewModel {
                    LogsViewModel(logRepository)
                },
                innerPadding = innerPadding
            )
        }
        composable(route = BottomNavItem.Heartbeat.route) {
            HeartBeatScreen(
                viewModel = viewModel {
                    HeartbeatViewModel(logRepository)
                },
                innerPadding = innerPadding
            )
        }
    }
}
