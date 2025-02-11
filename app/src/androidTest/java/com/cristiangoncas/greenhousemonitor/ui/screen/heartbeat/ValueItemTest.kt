package com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class ValueItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testValueItem_whenActionClicked_thenOutlinesTextFieldIsFilled(): Unit =
        with(composeTestRule) {
            val maxTempFlow = MutableStateFlow("22")
            setContent {
                val state = maxTempFlow.collectAsState()
                ValueItem(
                    label = "Max Temp",
                    value = state.value,
                    validateAndSend = { newValue -> maxTempFlow.value = newValue }
                )
            }
            val maxTemp = "23"
            onNodeWithTag(VALUE_ITEM_OUTLINE_TEXT_FIELD_TAG).assert(hasText("22"))
            onNodeWithTag(VALUE_ITEM_OUTLINE_TEXT_FIELD_TAG).performClick()
                .performTextReplacement(maxTemp)
            onNodeWithTag(VALUE_ITEM_OUTLINE_TEXT_FIELD_TAG).assert(hasText(maxTemp))
            onNodeWithTag(VALUE_ITEM_BUTTON_TAG).performClick()
            onNodeWithTag(VALUE_ITEM_INPUT_TEXT_FIELD_TAG).assert(hasText("($maxTemp)"))
        }
}