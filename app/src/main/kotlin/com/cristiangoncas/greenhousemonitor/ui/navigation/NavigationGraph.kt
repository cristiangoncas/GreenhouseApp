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
import com.cristiangoncas.greenhousemonitor.data.remote.client.ApiImpl
import com.cristiangoncas.greenhousemonitor.data.local.db.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.data.repository.HeartbeatRepository
import com.cristiangoncas.greenhousemonitor.data.repository.HeartbeatRepositoryImpl
import com.cristiangoncas.greenhousemonitor.data.local.LocalDataSource
import com.cristiangoncas.greenhousemonitor.data.local.LocalDataSourceImpl
import com.cristiangoncas.greenhousemonitor.data.repository.LogsRepository
import com.cristiangoncas.greenhousemonitor.data.repository.LogsRepositoryImpl
import com.cristiangoncas.greenhousemonitor.data.remote.RemoteDataSource
import com.cristiangoncas.greenhousemonitor.data.remote.RemoteDataSourceImpl
import com.cristiangoncas.greenhousemonitor.ui.common.rememberConnectivityState
import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartBeatScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartbeatViewModel
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeViewModel
import com.cristiangoncas.greenhousemonitor.ui.screen.logs.LogsScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.logs.LogsViewModel
import com.cristiangoncas.greenhousemonitor.ui.usecases.Average12hUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.Average24hUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.Average48hUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.FetchLogs24hUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.HeaterEvents24hUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.NextHeartbeatUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.RequestHealthCheckUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.ResetDefaultParamsUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.SetHeartbeatPeriodUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.SetMaxTempUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.SetMinTempUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.SetMorningTimeUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.SetNightTempDifferenceUseCase
import com.cristiangoncas.greenhousemonitor.ui.usecases.SetNightTimeUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

@Composable
fun NavigationGraph(navHostController: NavHostController, innerPadding: PaddingValues) {
    val context: Context = LocalContext.current.applicationContext
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val connectivityState = rememberConnectivityState(connectivityManager)

    // TODO: This will change once I introduce dependency injection
    val httpClient = HttpClient(Android)
    val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl(
        api = ApiImpl(client = httpClient, apiUrl = BuildConfig.API_IP)
    )
    val logRepository: LocalDataSource = LocalDataSourceImpl(
        db = GreenhouseDB.getInstance(context)
    )
    val logsRepository: LogsRepository = LogsRepositoryImpl(
        remoteDataSource = remoteDataSource,
        localDataSource = logRepository,
        connectivityState = connectivityState
    )
    val logs24hUseCase = FetchLogs24hUseCase(logsRepository)
    val heartbeatRepository: HeartbeatRepository = HeartbeatRepositoryImpl(remoteDataSource)

    val average12hUseCase = Average12hUseCase(logsRepository)
    val average24hUseCase = Average24hUseCase(logsRepository)
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
                viewModel = viewModel {
                    HomeViewModel(
                        average12hUseCase,
                        average24hUseCase,
                        average48hUseCase,
                        heaterEvents24hUseCase
                    )
                },
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
