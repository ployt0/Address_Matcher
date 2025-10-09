package uk.co.codipy.addressmatcher

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import uk.co.codipy.addressmatcher.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import uk.co.codipy.addressmatcher.ui.screen.MainScreen


private fun swipeToL1Page(rule: androidx.compose.ui.test.junit4.ComposeTestRule) {
    rule.onNodeWithTag("MainPager").performTouchInput { swipeLeft() }
}

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

class L1InputTest {

    @get:Rule val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testL1AutocompleteSuggests() {
        composeTestRule.setContent {
            AppTheme {
                MainScreen(16.dp, ocToL1s, l1sToOCs)
            }
        }
        swipeToL1Page(composeTestRule)

        composeTestRule.onNodeWithText("L1").performClick()
        composeTestRule.onNodeWithText("Portsmouth").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("L1").performTextInput("Por")
        composeTestRule.waitForIdle()
        // Waiting 1000ms is ample.
        composeTestRule.waitUntil { composeTestRule.onNodeWithText("Portsmouth").isDisplayed() }
        composeTestRule.onNodeWithText("Portsmouth").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testL1AutocompleteClickResults() {
        composeTestRule.setContent {
            AppTheme {
                MainScreen(16.dp, ocToL1s, l1sToOCs)
            }
        }
        swipeToL1Page(composeTestRule)

        composeTestRule.onNodeWithText("L1").performClick()
        composeTestRule.onNodeWithText("L1").performTextInput("Por")
        composeTestRule.onNodeWithText("OCs").assertIsDisplayed()
        composeTestRule.waitUntil { composeTestRule.onNodeWithText("Portsmouth").isDisplayed() }

        // This is the result we will want to seek displayed for us shortly:
        composeTestRule.onNodeWithText("PO").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Portsmouth").performClick()

        composeTestRule.waitUntil { composeTestRule.onNodeWithText("PO").isDisplayed() }
        composeTestRule.onNodeWithText("PO").assertIsDisplayed()
    }
}
