package com.bzolyomi.shoppinglist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bzolyomi.shoppinglist.data.DummyData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ComposeInstrumentedTest {

    @get:Rule(order = 1)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltTestRule.inject()
    }

    @Test
    fun testComposeWithFakeInput() {
        DummyData.groupsWithList.forEach{
            composeTestRule.onNodeWithText(it.toString()).assertIsDisplayed()
        }
    }

    @Test
    fun testComposeNavigationToGroupScreen() {
        composeTestRule.onAllNodesWithText("To GroupScreen")[0].performClick()
        composeTestRule.onNodeWithText(
            "Screen: GroupScreen of group: ${DummyData.groupsWithList[0].group.groupName}"
        ).assertIsDisplayed()
    }
}
