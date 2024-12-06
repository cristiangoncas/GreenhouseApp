package com.cristiangoncas.greenhousemonitor.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cristiangoncas.greenhousemonitor.BuildConfig
import com.cristiangoncas.greenhousemonitor.data.remote.client.ApiClient
import com.cristiangoncas.greenhousemonitor.data.local.db.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.data.repository.HeartbeatRepository
import com.cristiangoncas.greenhousemonitor.data.repository.HeartbeatRepositoryImpl
import com.cristiangoncas.greenhousemonitor.data.repository.LocalRepository
import com.cristiangoncas.greenhousemonitor.data.repository.LocalRepositoryImpl
import com.cristiangoncas.greenhousemonitor.data.repository.LogsRepository
import com.cristiangoncas.greenhousemonitor.data.repository.LogsRepositoryImpl
import com.cristiangoncas.greenhousemonitor.data.repository.RemoteRepository
import com.cristiangoncas.greenhousemonitor.data.repository.RemoteRepositoryImpl
import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartBeatScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartbeatViewModel
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeViewModel
import com.cristiangoncas.greenhousemonitor.ui.screen.logs.LogsScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.logs.LogsViewModel

@Composable
fun NavigationGraph(navHostController: NavHostController, innerPadding: PaddingValues) {
    val context: Context = LocalContext.current.applicationContext

    // TODO: This will change once I introduce dependency injection
    val remoteRepository: RemoteRepository = RemoteRepositoryImpl(
        api = ApiClient(apiUrl = BuildConfig.API_IP)
    )
    val logRepository: LocalRepository = LocalRepositoryImpl(
        remoteRepository = remoteRepository,
        db = GreenhouseDB.getInstance(context)
    )
    val logsRepository: LogsRepository = LogsRepositoryImpl(logRepository)
    val heartbeatRepository: HeartbeatRepository = HeartbeatRepositoryImpl(remoteRepository)

    NavHost(
        navController = navHostController,
        startDestination = BottomNavItem.Home.route,
    ) {
        composable(route = BottomNavItem.Home.route) {
            HomeScreen(
                viewModel = viewModel {
                    HomeViewModel(logsRepository)
                },
                innerPadding = innerPadding
            )
        }
        composable(route = BottomNavItem.Logs.route) {
            LogsScreen(
                viewModel = viewModel {
                    LogsViewModel(logsRepository)
                },
                innerPadding = innerPadding
            )
        }
        composable(route = BottomNavItem.Heartbeat.route) {
            HeartBeatScreen(
                viewModel = viewModel {
                    HeartbeatViewModel(heartbeatRepository)
                },
                innerPadding = innerPadding
            )
        }
    }
}
