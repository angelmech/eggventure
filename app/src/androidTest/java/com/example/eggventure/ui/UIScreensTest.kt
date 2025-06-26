package com.example.eggventure.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import com.example.eggventure.MainActivity


class UIScreensTest {

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
        composeTestRule
            .onNodeWithText("523 / 5000 Schritten", substring = true)
            .assertExists()
            .assertIsDisplayed()

    }

    @Test
    fun fakeStepButton_visibilityDependsOnTrackingState() {
        // Button nicht sichtbar am Anfang
        composeTestRule
            .onNodeWithText("Schritte simulieren")
            .assertExists()
            .assertIsNotEnabled()

        // Tracking starten
        composeTestRule
            .onNodeWithText("Lauf Starten")
            .performClick()

        // Jetzt sichtbar & enabled
        composeTestRule
            .onNodeWithText("Schritte simulieren")
            .assertIsEnabled()
            .assertIsDisplayed()
    }

    @Test
    fun navigationBar_allScreensNavigable() {
        // Navigiere zu "Lauf" und überprüfe die Anzeige des Start-Buttons
        composeTestRule.onNodeWithText("Lauf").performClick()
        composeTestRule.onNodeWithText("Lauf").assertIsDisplayed()

        // Navigiere zu "Sammlung" und überprüfe die Anzeige der Sammlung
        composeTestRule.onNodeWithText("Sammlung").performClick()
        composeTestRule.onNodeWithText("Sammlung").assertIsDisplayed()

        // Navigiere zu "Stats" und überprüfe die Anzeige der Statistik
        composeTestRule.onNodeWithText("Stats").performClick()
        composeTestRule.onNodeWithText("Stats").assertIsDisplayed()
    }



    @Test
    fun progressBar_updatesOnStepIncrease() {
        composeTestRule.onNodeWithText("Lauf Starten").performClick()

        composeTestRule.onNodeWithText("0 / 5000 Schritten").assertIsDisplayed()

        composeTestRule.onNodeWithText("Schritte simulieren").performClick()

        composeTestRule.onNodeWithText("523 / 5000 Schritten").assertIsDisplayed()
    }



}