package com.cristiangoncas.greenhousemonitor.ui.screen.logs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cristiangoncas.data.remote.ConnectivityDataSource
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import com.cristiangoncas.greenhousemonitor.ui.common.LOADING_INDICATOR_TAG
import com.cristiangoncas.greenhousemonitor.ui.screen.KoinTestRule
import com.cristiangoncas.usecases.FetchLastLogsUseCase
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.compose.get
import org.koin.compose.koinInject
import org.koin.dsl.module

class LogsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLogsScreen_whenLoading_thenShowProgress(): Unit = with(composeTestRule) {
        setContent {
            LogsScreen(
                innerPadding = PaddingValues.Absolute(),
                isConnected = true,
                state = LogsViewModel.UiState(loading = true),
                onUiReady = {},
                onRefresh = {}
            )
        }
        onNodeWithTag(LOADING_INDICATOR_TAG).assertIsDisplayed()
    }

    @Test
    fun testLogsScreen_whenListNotEmpty_ThenTwoLogsDisplayed(): Unit = with(composeTestRule) {
        setContent {
            val logs = listOf(
                LogEntry(
                    id = 1,
                    timestamp = 0,
                    date = "12-12-2022",
                    time = "12:12:12",
                    data = "On",
                    event = "Heater on"
                ),
                LogEntry(
                    id = 2,
                    timestamp = 0,
                    date = "12-12-2022",
                    time = "12:12:12",
                    data = "56.00",
                    event = "Humidity read"
                )
            )
            LogsScreen(
                innerPadding = PaddingValues.Absolute(),
                isConnected = true,
                state = LogsViewModel.UiState(loading = false, logs = logs),
                onUiReady = {},
                onRefresh = {}
            )
        }
        onNodeWithTag(LOADING_INDICATOR_TAG).assertIsNotDisplayed()
        onNodeWithTag(LOG_ITEM_TAG + 1).assertIsDisplayed()
        onNodeWithTag(LOG_ITEM_TAG + 2).assertIsDisplayed()
        onNodeWithTag(LOG_ITEM_TAG + 3).assertIsNotDisplayed()
    }
}