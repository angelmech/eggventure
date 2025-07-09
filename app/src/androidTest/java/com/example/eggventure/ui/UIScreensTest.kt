package com.example.eggventure.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import com.example.eggventure.MainActivity


class UIScreensTest {

    // auto permission for activity recognition
    @get:Rule(order = 0)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACTIVITY_RECOGNITION
    )

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun startButton_toggleTracking_showsCorrectText() {
        composeTestRule
            .onNodeWithText("Lauf Starten")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithText("Stopp")
            .assertIsDisplayed()
    }

    @Test
    fun stepProgress_displaysCorrectSteps() {
        // Start tracking
        composeTestRule.onNodeWithText("Lauf Starten").performClick()

        // Simulate steps
        composeTestRule.onNodeWithText("Schritte simulieren")
            .assertIsDisplayed()
            .performClick()

        // Wait for any " / 5000 Schritte" text to appear
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithText("/ 5000", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Check the step progress is shown
        composeTestRule.onNodeWithText("/ 5000", substring = true)
            .assertExists()
            .assertIsDisplayed()
    }


    @Test
    fun fakeStepButton_visibilityDependsOnTrackingState() {
        composeTestRule
            .onNodeWithText("Schritte simulieren")
            .assertExists()
            .assertIsNotEnabled()

        composeTestRule
            .onNodeWithText("Lauf Starten")
            .performClick()

        composeTestRule
            .onNodeWithText("Schritte simulieren")
            .assertIsEnabled()
            .assertIsDisplayed()
    }

    @Test
    fun navigationBar_allScreensNavigable() {
        composeTestRule.onNode(
            hasText("Lauf") and hasClickAction()
        ).performClick()

        composeTestRule.onNode(
            hasText("Lauf") and hasClickAction()
        ).assertIsDisplayed()

        composeTestRule.onNode(
            hasText("Sammlung") and hasClickAction()
        ).performClick()

        composeTestRule.onNode(
            hasText("Sammlung") and hasClickAction()
        ).assertIsDisplayed()

        composeTestRule.onNode(
            hasText("Stats") and hasClickAction()
        ).performClick()

        composeTestRule.onNode(
            hasText("Stats") and hasClickAction()
        ).assertIsDisplayed()
    }



    @Test
    fun progressBar_updatesOnStepIncrease() {
        // Start the run
        composeTestRule.onNodeWithText("Lauf Starten").performClick()

        // Wait for the initial progress text to appear
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithText("0 / 5000", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("0 / 5000", substring = true)
            .assertIsDisplayed()

        // Simulate step increase
        composeTestRule.onNodeWithText("Schritte simulieren")
            .assertIsDisplayed()
            .performClick()

        // Wait for the updated step count to appear
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithText("523 / 5000", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("523 / 5000", substring = true)
            .assertIsDisplayed()
    }





}