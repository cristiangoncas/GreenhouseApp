package com.cristiangoncas.greenhousemonitor.ui.screen.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.cristiangoncas.greenhousemonitor.domain.models.Average
import com.cristiangoncas.greenhousemonitor.domain.models.EventCount
import com.cristiangoncas.greenhousemonitor.ui.common.LOADING_INDICATOR_TAG
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomeScreen_whenLoading_thenShowProgress(): Unit = with(composeTestRule) {
        setContent {
            HomeScreen(
                state = HomeViewModel.UiState(loading = true),
                onUiReady = {},
                onRefresh = {}
            )
        }
        onNodeWithTag(LOADING_INDICATOR_TAG).assertIsDisplayed()
    }

    @Test
    fun testHomeScreen_whenScreenNotEmpty_thenThreeAveragesAreDisplayed(): Unit = with(composeTestRule) {
        setContent {
            HomeScreen(
                state = HomeViewModel.UiState(
                    loading = false,
                    averages = HomeViewModel.UiState.AveragesUiState(
                        avg12h = Average(
                            avgTemp = 22f,
                            avgHumid = 78f,
                            hours = 12
                        ),
                        avg24h = Average(
                            avgTemp = 22f,
                            avgHumid = 78f,
                            hours = 24
                        ),
                        avg48h = Average(
                            avgTemp = 22f,
                            avgHumid = 78f,
                            hours = 48
                        )
                    ),
                    events = HomeViewModel.UiState.EventsUiState(
                        heaterOn = EventCount(
                            event = "Heater on",
                            count = 2,
                            hours = 24
                        )
                    )
                ),
                onUiReady = {},
                onRefresh = {}
            )
        }
        onNodeWithTag(AVERAGE_COMPONENT_TAG + 12).assertIsDisplayed()
        onNodeWithTag(AVERAGE_COMPONENT_TAG + 24).assertIsDisplayed()
        onNodeWithTag(AVERAGE_COMPONENT_TAG + 48).assertIsDisplayed()
        onNodeWithTag(EVENT_COUNT_COMPONENT_TAG + 24).assertIsDisplayed()
        onNodeWithTag(EVENT_COUNT_COMPONENT_TAG + 25).assertDoesNotExist()
    }
}
