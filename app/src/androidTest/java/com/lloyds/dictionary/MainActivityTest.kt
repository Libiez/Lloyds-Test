package com.lloyds.dictionary

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.lloyds.dictionary.MainActivity.Companion.SEARCH_TESTIFIED
import com.lloyds.dictionary.dictionary.domain.model.Definition
import com.lloyds.dictionary.dictionary.domain.model.Meaning
import com.lloyds.dictionary.dictionary.domain.model.WordInfo
import com.lloyds.dictionary.dictionary.presenation.WordInfoItem
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var androidComposeTestRule = createAndroidComposeRule(MainActivity::class.java)

    @Test
    fun searchFiledIsDisplayed() {
        androidComposeTestRule.setContent {

            androidComposeTestRule.activity.SearchScreen()

        }
        androidComposeTestRule.onNode(hasTestTag(SEARCH_TESTIFIED))
            .assertIsDisplayed()
    }

    @Test
    fun testTextFieldContainValues() {
        androidComposeTestRule.setContent {

            androidComposeTestRule.activity.SearchScreen()
        }
        androidComposeTestRule.onRoot().printToLog("SearchScreen")

        androidComposeTestRule.onNode(hasTestTag(SEARCH_TESTIFIED))
            .performTextInput("Bank")

        androidComposeTestRule.onNode(hasTestTag(SEARCH_TESTIFIED))
            .assertTextContains("Bank")
    }

    @Test
    fun test_Dictionary_ComposesWithData() {
        androidComposeTestRule.setContent {
            WordInfoItem(returnCustomList()[0])
        }
        androidComposeTestRule.onNodeWithText("bank")
            .assertExists().assertExists().assertTextEquals("bank").assertIsDisplayed()

        androidComposeTestRule.onNodeWithText("bæŋk")
            .assertExists().assertIsDisplayed().assertTextEquals("bæŋk")

        androidComposeTestRule.onNodeWithText("Example: bloodBank 1")
            .assertExists().assertIsDisplayed().assertTextEquals("Example: bloodBank 1")
    }

    private fun returnCustomList(): List<WordInfo> {
        val definitions = mutableListOf<Definition>()
        for (i in 1..5) {
            definitions.add(
                Definition(
                    definition = "institution $i",
                    example = "bloodBank $i",
                    synonyms = null,
                    antonyms = null
                )
            )
        }
        val meaning1 = Meaning(definitions, "noun")
        val meaning2 = Meaning(definitions, "noun")
        val meanings: List<Meaning> = listOf(meaning1, meaning2)
        val wordInfoEntity = WordInfo(meanings, "bæŋk", "bank")
        return listOf(wordInfoEntity)
    }


}