package com.bzolyomi.shoppinglist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_FULL_DURATION
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.concurrent.schedule

@HiltAndroidTest
class ShoppingListComposeTest {

    @get:Rule(order = 1)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltTestRule.inject()
    }

    @Test
    fun testAllGroupsScreen() {
        waitUntilIntroScreenIsDone()
        composeTestRule.onNodeWithText("Spar").assertIsDisplayed()
    }

    private fun waitUntilIntroScreenIsDone() {
        var isIntroScreenDone = false
        composeTestRule.waitUntil(
            condition = {
                Timer().schedule(INTRO_SCREEN_FULL_DURATION) { isIntroScreenDone = true }
                isIntroScreenDone
            },
            timeoutMillis = INTRO_SCREEN_FULL_DURATION + 1000
        )
    }
}