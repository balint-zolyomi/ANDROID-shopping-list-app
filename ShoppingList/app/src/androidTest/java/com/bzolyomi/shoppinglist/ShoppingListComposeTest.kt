package com.bzolyomi.shoppinglist

import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_FULL_DURATION
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
    fun testGroupText() {
        waitUntilIntroScreenIsDone()
        composeTestRule.onNodeWithText("Spar").assertIsDisplayed()
    }

    @Test
    fun testItemText() {
        waitUntilIntroScreenIsDone()
        composeTestRule.onNodeWithText("Spar").performClick()
        composeTestRule.onNodeWithText("cheese -- 2 kg").assertIsDisplayed()
    }

    @Test
    fun testAddGroupAndItemFAB() {
        waitUntilIntroScreenIsDone()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        composeTestRule.onNodeWithContentDescription(
            context.resources.getString(
                R.string.content_description_fab_add_group_and_item
            )
        ).performClick()
        composeTestRule.onNodeWithText("Add item").assertIsDisplayed()
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