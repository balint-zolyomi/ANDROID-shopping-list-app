package com.bzolyomi.shoppinglist

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso
import androidx.test.platform.app.InstrumentationRegistry
import com.bzolyomi.shoppinglist.data.DummyData
import com.bzolyomi.shoppinglist.data.ShoppingGroupEntity
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_FULL_DURATION
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.concurrent.schedule

@HiltAndroidTest
class ComposeInstrumentedTest {
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule(order = 1)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltTestRule.inject()
        waitUntilIntroScreenIsDone()
        createDummyData()
    }

    @Test
    fun testHomeScreenGroupTexts() {
        DummyData.groupsWithList.forEach {
            composeTestRule.onNodeWithText(it.group.groupName).assertIsDisplayed()
        }
    }

    @Test
    fun testHomeScreenItemTexts() {
        DummyData.groupsWithList.forEach { group ->
            composeTestRule.onNodeWithText(group.group.groupName).performClick()
            group.shoppingList.forEach { item ->
                composeTestRule.onNode(hasText(item.itemName, true)).assertIsDisplayed()
            }
        }
    }

    @Test
    fun testGroupScreenGroupText() {
        DummyData.groupsWithList.forEach {
            goToGroupScreenOf(it.group)
            composeTestRule.onNode(hasText(it.group.groupName)).assertIsDisplayed()
            Espresso.pressBack()
        }
    }

    @Test
    fun testAddScreenGroupText() {
        DummyData.groupsWithList.forEach {
            goToGroupScreenOf(it.group)
            clickOnAddItemFAB()
            composeTestRule.onNodeWithText(it.group.groupName).assertIsDisplayed()
            Espresso.pressBack()
            Espresso.pressBack()
        }
    }

    @Test
    fun testGroupScreenItemTexts() {
        DummyData.groupsWithList.forEach {
            goToGroupScreenOf(it.group)
            it.shoppingList.forEach { item ->
                composeTestRule.onNode(hasText(item.itemName, true)).assertIsDisplayed()
            }
            Espresso.pressBack()
        }
    }

    @Test
    fun testDeleteGroup() {
        DummyData.groupsWithList.forEach {
            goToGroupScreenOf(it.group)
            clickOnDropdownMenu()
            clickOnDeleteGroupOption()
            clickOnConfirm()
            composeTestRule.onNodeWithText(it.group.groupName).assertDoesNotExist()
        }
    }

    @Test
    fun testDeleteItem() {
        DummyData.groupsWithList.forEach {
            goToGroupScreenOf(it.group)

            val randomItemName = it.shoppingList.random().itemName
            clickOnDeleteItemIconOf(randomItemName)

            Espresso.pressBack()
            clickOnGroupCard(it.group.groupName)
            composeTestRule.onNode(hasText(randomItemName, true)).assertDoesNotExist()

            clickOnOpenInNewIcon()
            composeTestRule.onNodeWithText(randomItemName, true).assertDoesNotExist()
            Espresso.pressBack()
        }
    }

    @Test
    fun testMarkItemAsDone() {
        DummyData.groupsWithList.forEach { groupWithList ->
            goToGroupScreenOf(groupWithList.group)

            val randomItemName = groupWithList.shoppingList.random().itemName
            markItemAsDone(randomItemName)

            Espresso.pressBack()
            clickOnGroupCard(groupWithList.group.groupName)
            composeTestRule.onNode(hasText(randomItemName, true)).assertIsDisplayed()

            clickOnOpenInNewIcon()
            composeTestRule.onNodeWithContentDescription(
                context.resources.getString(
                    R.string.content_description_icon_checkbox_item_done
                ) + " (item: $randomItemName)"
            ).assertIsDisplayed()
            Espresso.pressBack()
        }
    }

    // Util
    private fun waitUntilIntroScreenIsDone() {
        var isIntroScreenDone = false
        composeTestRule.waitUntil(
            condition = {
                Timer().schedule(INTRO_SCREEN_FULL_DURATION) { isIntroScreenDone = true }
                isIntroScreenDone
            }, timeoutMillis = INTRO_SCREEN_FULL_DURATION + 2000
        )
    }

    // Aggregated actions
    private fun createDummyData() {
        deleteAll()
        DummyData.groupsWithList.forEach { groupWithList ->
            clickOnAddFAB()
            inputGroupName(groupWithList.group.groupName)
            groupWithList.shoppingList.forEach { item ->
                inputItemName(item.itemName)
                inputItemQuantity(item.itemQuantity)
                inputItemUnit(item.itemUnit)
                if (item != groupWithList.shoppingList.last()) clickOnAddItemButton()
            }
            clickOnConfirmFAB()
        }
    }

    private fun deleteAll() {
        clickOnDropdownMenu()
        clickOnDeleteAllOption()
        clickOnConfirm()
    }

    // Navigation
    private fun goToGroupScreenOf(group: ShoppingGroupEntity) {
        composeTestRule.onNodeWithText(group.groupName).performClick()
        clickOnOpenInNewIcon()
    }

    // ONCLICKS
    // groupCard
    private fun clickOnGroupCard(groupName: String) {
        composeTestRule.onNodeWithText(groupName).performClick()
    }

    private fun clickOnOpenInNewIcon() {
        composeTestRule.onNodeWithContentDescription(
            context.resources.getString(R.string.content_description_icon_open_in_new)
        ).performClick()
    }

    // itemIcons
    private fun clickOnDeleteItemIconOf(itemName: String) {
        composeTestRule.onNodeWithContentDescription(
            context.resources.getString(
                R.string.content_description_icon_delete_item
            ) + " $itemName"
        ).performClick()
    }

    private fun markItemAsDone(itemName: String) {
        composeTestRule.onNodeWithContentDescription(
            context.resources.getString(
                R.string.content_description_icon_checkbox_item_not_done
            ) + " (item: $itemName)"
        ).performClick()
    }

    // FABs
    private fun clickOnAddFAB() {
        composeTestRule.onNodeWithContentDescription(
            context.resources.getString(
                R.string.content_description_fab_add_group_and_item
            )
        ).performClick()
    }

    private fun clickOnConfirmFAB() {
        composeTestRule.onNodeWithContentDescription(
            context.resources.getString(
                R.string.content_description_fab_confirm_add
            )
        ).performClick()
    }

    private fun clickOnAddItemFAB() {
        composeTestRule.onNodeWithContentDescription(
            context.resources.getString(
                R.string.content_description_fab_add_item
            )
        ).performClick()
    }

    // Buttons
    private fun clickOnAddItemButton() {
        composeTestRule.onNode(
            hasText(
                context.resources.getString(
                    R.string.button_add_item_text
                )
            )
        ).performClick()
    }

    private fun clickOnConfirm() {
        composeTestRule.onNode(
            hasText(
                context.resources.getString(
                    R.string.button_confirm_text
                )
            )
        ).performClick()
    }

    // Menu options
    private fun clickOnDropdownMenu() {
        composeTestRule.onNodeWithContentDescription(
            context.resources.getString(R.string.content_description_icon_open_dropdown_menu)
        ).performClick()
    }

    private fun clickOnDeleteAllOption() {
        composeTestRule.onNode(
            hasText(
                context.resources.getString(
                    R.string.appbar_dropdown_menu_option_delete_all
                )
            )
        ).performClick()
    }

    private fun clickOnDeleteGroupOption() {
        composeTestRule.onNode(
            hasText(
                context.resources.getString(
                    R.string.appbar_dropdown_menu_option_delete_group
                )
            )
        ).performClick()
    }

    // Inputs
    private fun inputGroupName(groupName: String) {
        composeTestRule.onNode(
            hasText(
                context.resources.getString(R.string.input_label_group_name)
            )
        ).performClick().performTextInput(groupName)
    }

    private fun inputItemName(itemName: String) {
        composeTestRule.onNode(
            hasText(
                context.resources.getString(R.string.input_label_item_name)
            )
        ).performClick().performTextInput(itemName)
    }

    private fun inputItemUnit(itemUnit: String) {
        composeTestRule.onNode(
            hasText(
                context.resources.getString(R.string.input_label_item_unit)
            )
        ).performClick().performTextInput(itemUnit)
    }

    private fun inputItemQuantity(itemQuantity: Float?) {
        composeTestRule.onNode(
            hasText(
                context.resources.getString(R.string.input_label_item_quantity)
            )
        ).performClick().performTextInput(itemQuantity.toString())
    }
}
