package com.cristiangoncas.greenhousemonitor.ui.navigation

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cristiangoncas.greenhousemonitor.BuildConfig
import com.cristiangoncas.greenhousemonitor.framework.remote.client.ApiImpl
import com.cristiangoncas.greenhousemonitor.framework.local.database.GreenhouseDB
import com.cristiangoncas.data.repository.HeartbeatRepository
import com.cristiangoncas.data.repository.HeartbeatRepositoryImpl
import com.cristiangoncas.data.local.LocalDataSource
import com.cristiangoncas.data.repository.LogsRepository
import com.cristiangoncas.data.repository.LogsRepositoryImpl
import com.cristiangoncas.data.remote.RemoteDataSource
import com.cristiangoncas.greenhousemonitor.framework.local.RoomDataSource
import com.cristiangoncas.greenhousemonitor.framework.remote.APIDataSource
import com.cristiangoncas.greenhousemonitor.ui.common.ConnectivityState
import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartBeatScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartbeatViewModel
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeViewModel
import com.cristiangoncas.greenhousemonitor.ui.screen.logs.LogsScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.logs.LogsViewModel
import com.cristiangoncas.usecases.Average12hUseCase
import com.cristiangoncas.usecases.Average24hUseCase
import com.cristiangoncas.usecases.Average48hUseCase
import com.cristiangoncas.usecases.FetchLogs24hUseCase
import com.cristiangoncas.usecases.HeaterEvents24hUseCase
import com.cristiangoncas.usecases.NextHeartbeatUseCase
import com.cristiangoncas.usecases.RequestHealthCheckUseCase
import com.cristiangoncas.usecases.ResetDefaultParamsUseCase
import com.cristiangoncas.usecases.SetHeartbeatPeriodUseCase
import com.cristiangoncas.usecases.SetMaxTempUseCase
import com.cristiangoncas.usecases.SetMinTempUseCase
import com.cristiangoncas.usecases.SetMorningTimeUseCase
import com.cristiangoncas.usecases.SetNightTempDifferenceUseCase
import com.cristiangoncas.usecases.SetNightTimeUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

@Composable
fun NavigationGraph(navHostController: NavHostController, innerPadding: PaddingValues) {
    val context: Context = LocalContext.current.applicationContext
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val connectivityState = ConnectivityState(connectivityManager)

    val httpClient = HttpClient(Android)
    val remoteDataSource: RemoteDataSource = APIDataSource(
        api = ApiImpl(client = httpClient, apiUrl = BuildConfig.API_IP)
    )

    val logRepository: LocalDataSource = RoomDataSource(
        db = GreenhouseDB.getInstance(context)
    )
    val logsRepository: LogsRepository = LogsRepositoryImpl(
        remoteDataSource = remoteDataSource,
        localDataSource = logRepository,
        connectivityState = connectivityState
    )
    val logs24hUseCase = FetchLogs24hUseCase(logsRepository)
    val heartbeatRepository: HeartbeatRepository = HeartbeatRepositoryImpl(remoteDataSource)
    val average48hUseCase = Average48hUseCase(logsRepository)
    val heaterEvents24hUseCase = HeaterEvents24hUseCase(logsRepository)

    val nextHeartbeatUseCase = NextHeartbeatUseCase(heartbeatRepository)
    val setMaxTempUseCase = SetMaxTempUseCase(heartbeatRepository)
    val setMinTempUseCase = SetMinTempUseCase(heartbeatRepository)
    val setMorningTimeUseCase = SetMorningTimeUseCase(heartbeatRepository)
    val setNightTempUseCase = SetNightTimeUseCase(heartbeatRepository)
    val setNightTempDifferenceUseCase = SetNightTempDifferenceUseCase(heartbeatRepository)
    val requestHealthCheckUseCase = RequestHealthCheckUseCase(heartbeatRepository)
    val resetDefaultParamsUseCase = ResetDefaultParamsUseCase(heartbeatRepository)
    val setHeartbeatPeriodUseCase = SetHeartbeatPeriodUseCase(heartbeatRepository)

    NavHost(
        navController = navHostController,
        startDestination = BottomNavItem.Home.route,
    ) {
        composable(route = BottomNavItem.Home.route) {
            HomeScreen(
                innerPadding = innerPadding
            )
        }
        composable(route = BottomNavItem.Logs.route) {
            LogsScreen(
                viewModel = viewModel {
                    LogsViewModel(logs24hUseCase)
                },
                innerPadding = innerPadding,
                connectivityState = connectivityState
            )
        }
        composable(route = BottomNavItem.Heartbeat.route) {
            HeartBeatScreen(
                viewModel = viewModel {
                    HeartbeatViewModel(
                        nextHeartbeatUseCase,
                        setMaxTempUseCase,
                        setMinTempUseCase,
                        setMorningTimeUseCase,
                        setNightTempUseCase,
                        setNightTempDifferenceUseCase,
                        requestHealthCheckUseCase,
                        resetDefaultParamsUseCase,
                        setHeartbeatPeriodUseCase
                    )
                },
                innerPadding = innerPadding
            )
        }
    }
}
