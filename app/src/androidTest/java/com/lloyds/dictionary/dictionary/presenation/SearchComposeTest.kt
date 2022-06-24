package com.lloyds.dictionary.dictionary.presenation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import com.lloyds.dictionary.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SearchComposeTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createComposeRule()

    @Test
    fun searchFiledIsDisplayed() {
        rule.setContent {
         MainActivity().SearchScreen()
        }
        rule.onNode(hasTestTag(MainActivity.SEARCH_TEXTFIELD))
            .assertIsDisplayed()
    }

}