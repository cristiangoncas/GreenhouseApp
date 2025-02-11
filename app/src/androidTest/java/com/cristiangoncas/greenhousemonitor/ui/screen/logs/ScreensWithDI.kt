package com.cristiangoncas.greenhousemonitor.ui.screen.logs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.cristiangoncas.data.local.LocalDataSource
import com.cristiangoncas.data.remote.ConnectivityDataSource
import com.cristiangoncas.data.remote.RemoteDataSource
import com.cristiangoncas.data.repository.LogsRepository
import com.cristiangoncas.greenhousemonitor.framework.local.RoomDataSource
import com.cristiangoncas.greenhousemonitor.framework.local.database.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.framework.local.database.LogEntryDao
import com.cristiangoncas.greenhousemonitor.framework.remote.APIDataSource
import com.cristiangoncas.greenhousemonitor.framework.remote.client.Api
import com.cristiangoncas.greenhousemonitor.ui.screen.KoinTestRule
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeScreen
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeViewModel
import com.cristiangoncas.usecases.Average12hUseCase
import com.cristiangoncas.usecases.Average24hUseCase
import com.cristiangoncas.usecases.Average48hUseCase
import com.cristiangoncas.usecases.FetchLastLogsUseCase
import com.cristiangoncas.usecases.HeaterEvents24hUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
class ScreensWithDI {


    val mockUiModule = module {
        viewModel { LogsViewModel(get()) }
        viewModel { HomeViewModel(get(), get(), get(), get()) }
    }

    val mockFrameworkModule = module {

    }

    val mockUseCaseModule = module {
        factory<ConnectivityDataSource> { MockConnectivityDataSource() }
        factory<FetchLastLogsUseCase> { FetchLastLogsUseCase(MockLogsRepository()) }
        factory<Average12hUseCase> { Average12hUseCase(MockLogsRepository()) }
        factory<Average24hUseCase> { Average24hUseCase(MockLogsRepository()) }
        factory<Average48hUseCase> { Average48hUseCase(MockLogsRepository()) }
        factory<HeaterEvents24hUseCase> { HeaterEvents24hUseCase(MockLogsRepository()) }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(listOf(mockUiModule, mockFrameworkModule, mockUseCaseModule))

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomeScreen_whenParametersInjected_thenLogsAreDisplayed(): Unit = with(composeTestRule) {
        setContent {
            HomeScreen()
        }
        onNodeWithText("Avg Temperature 12h")
            .assertIsDisplayed()
        onNodeWithText("Avg Humidity 24h")
            .assertIsDisplayed()
        onAllNodesWithText("18.0")
            .assertCountEquals(3)
        onAllNodesWithText("56.0")
            .assertCountEquals(3)
        onNodeWithText("Heater on events - 24h")
            .assertIsDisplayed()
    }

    @Test
    fun testLogsScreen_whenParametersInjected_thenLogsAreDisplayed(): Unit = with(composeTestRule) {
        setContent {
            LogsScreen(innerPadding = PaddingValues.Absolute())
        }
        onNodeWithText("This is a test!")
            .assertIsDisplayed()
        onNodeWithText("This is a test2!")
            .assertIsDisplayed()
    }

//    private val testDispatcher = StandardTestDispatcher()
//
//    val mockUiModule = module {
//        viewModel { LogsViewModel(get(), testDispatcher) }
//        viewModel { HomeViewModel(get(), get(), get(), get()) }
//        single<TestDb> { TestDb(get()) }
//    }
//
//    val mockFrameworkModule = module {
//        single<GreenhouseDB> {
//            GreenhouseDB.getTestInstance(get())
//        }
//        single<LogEntryDao> { get<GreenhouseDB>().logEntryDao() }
//        single<LocalDataSource> { RoomDataSource(get()) }
//        single<RemoteDataSource> { APIDataSource(get()) }
//        single<Api> { MockApi() }
//    }
//
//    val mockUseCaseModule = module {
//        factory<ConnectivityDataSource> { MockConnectivityDataSource() }
//        single<FetchLastLogsUseCase> { FetchLastLogsUseCase(get()) }
//        single<Average12hUseCase> { Average12hUseCase(get()) }
//        single<Average24hUseCase> { Average24hUseCase(get()) }
//        single<Average48hUseCase> { Average48hUseCase(get()) }
//        single<HeaterEvents24hUseCase> { HeaterEvents24hUseCase(get()) }
//    }
//
//    val mockDataModule = module {
//        single<LogsRepository> { MockLogsRepository() }
//    }
//
//    @get:Rule
//    val koinTestRule =
//        KoinTestRule(listOf(mockUiModule, mockFrameworkModule, mockUseCaseModule, mockDataModule))
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//
//    @Before
//    fun setUp() {
//        Dispatchers.setMain(testDispatcher)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun testHomeScreen_whenParametersInjected_thenLogsAreDisplayed(): Unit = with(composeTestRule) {
//        setContent {
//            HomeScreen()
//        }
//        testDispatcher.scheduler.advanceUntilIdle()
//        onNodeWithText("Avg Temperature 12h")
//            .assertIsDisplayed()
//        onNodeWithText("Avg Humidity 24h")
//            .assertIsDisplayed()
//        onAllNodesWithText("18.0")
//            .assertCountEquals(3)
//        onAllNodesWithText("56.0")
//            .assertCountEquals(3)
//        onNodeWithText("Heater on events - 24h")
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun testLogsScreen_whenParametersInjected_thenLogsAreDisplayed(): Unit = with(composeTestRule) {
//        setContent {
//            LogsScreen(innerPadding = PaddingValues.Absolute())
//
//        }
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        onNodeWithText("This is a test!")
//            .assertIsDisplayed()
//        onNodeWithText("This is a test2!")
//            .assertIsDisplayed()
//    }
}

