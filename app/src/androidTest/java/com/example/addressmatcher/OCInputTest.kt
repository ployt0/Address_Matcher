package uk.co.codipy.addressmatcher

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import uk.co.codipy.addressmatcher.ui.theme.AddressMatcherTheme
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight


private val ocToL1s: Map<String, List<String>> = mapOf<String, List<String>>(
    "EX" to listOf("Exeter", "Exmouth"),
    "PO" to listOf(
        "Portsmouth",
        "Isle of Wight"
    )
)
private val l1sToOCs: Map<String, List<String>> = mapOf<String, List<String>>(
    "Exeter" to listOf("EX"),
    "Portsmouth" to listOf("PO")
)

class OCInputTest {

    @get:Rule val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testOCAutocompleteSuggests() {
        composeTestRule.setContent {
            AddressMatcherTheme {
                MainScreen(16.dp, ocToL1s, l1sToOCs)
            }
        }

        composeTestRule.onNodeWithText("OC area").performClick()
        composeTestRule.onNodeWithText("EX").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("OC area").performTextInput("X")
        // Waiting 1000ms is ample.
        composeTestRule.waitUntil { composeTestRule.onNodeWithText("EX").isDisplayed() }
        composeTestRule.onNodeWithText("EX").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testOCAutocompleteClickResults() {
        composeTestRule.setContent {
            AddressMatcherTheme {
                MainScreen(16.dp, ocToL1s, l1sToOCs)
            }
        }

        composeTestRule.onNodeWithText("OC area").performClick()
        composeTestRule.onNodeWithText("OC area").performTextInput("X")
        composeTestRule.onNodeWithText("Result").assertIsDisplayed()
        composeTestRule.waitUntil { composeTestRule.onNodeWithText("EX").isDisplayed() }

        // This is the result we will want to seek displayed for us shortly:
        composeTestRule.onNodeWithText("Exeter\nExmouth").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("EX").performClick()

        composeTestRule.waitUntil { composeTestRule.onNodeWithText("Exeter\nExmouth").isDisplayed() }
        composeTestRule.onNodeWithText("Exeter\nExmouth").assertIsDisplayed()
    }
}