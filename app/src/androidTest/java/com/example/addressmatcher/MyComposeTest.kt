package com.example.addressmatcher

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertValueEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onSiblings
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.pressKey
import androidx.compose.ui.unit.dp
import com.example.addressmatcher.ui.theme.AddressMatcherTheme
import org.junit.Rule
import org.junit.Test

class MyComposeTest {

    @get:Rule val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testL1AutocompleteOffers() {
        // Start the app
        composeTestRule.setContent {
            AddressMatcherTheme {
                MainScreen(
                    16.dp,
                    mapOf<String, List<String>>(
                        "EX" to listOf("Exeter", "Exmouth"),
                        "PO" to listOf(
                            "Portsmouth",
                            "Isle of Wight"
                        )
                    ),
                    mapOf<String, List<String>>(
                        "Exeter" to listOf("EX"),
                        "Portsmouth" to listOf("PO")
                    ),
                )
            }
        }

        composeTestRule.onNodeWithText("OC area").performClick()
        composeTestRule.onNodeWithText("EX").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("OC area").performKeyInput {
            keyDown(Key.ShiftLeft)
            pressKey(Key.X, 200)
            keyUp(Key.ShiftLeft)
        }
        // Waiting 1000ms is ample.
        composeTestRule.waitUntil { composeTestRule.onNodeWithText("EX").isDisplayed() }
        composeTestRule.onNodeWithText("EX").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testL1AutocompleteClick() {
        // Start the app
        composeTestRule.setContent {
            AddressMatcherTheme {
                MainScreen(
                    16.dp,
                    mapOf<String, List<String>>(
                        "EX" to listOf("Exeter", "Exmouth"),
                        "PO" to listOf(
                            "Portsmouth",
                            "Isle of Wight"
                        )
                    ),
                    mapOf<String, List<String>>(
                        "Exeter" to listOf("EX"),
                        "Portsmouth" to listOf("PO")
                    ),
                )
            }
        }

        composeTestRule.onNodeWithText("OC area").performClick()
        composeTestRule.onNodeWithText("OC area").performKeyInput {
            keyDown(Key.ShiftLeft)
            pressKey(Key.X, 200)
            keyUp(Key.ShiftLeft)
        }
        composeTestRule.onNodeWithText("Result").assertIsDisplayed()
        composeTestRule.waitUntil { composeTestRule.onNodeWithText("EX").isDisplayed() }

        // This is the result we will want to seek displayed for us shortly:
        composeTestRule.onNodeWithText("Exeter\nExmouth").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("EX").performClick()

        composeTestRule.waitUntil { composeTestRule.onNodeWithText("Exeter\nExmouth").isDisplayed() }
        composeTestRule.onNodeWithText("Exeter\nExmouth").assertIsDisplayed()
    }
}