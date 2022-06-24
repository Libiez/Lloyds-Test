package com.lloyds.dictionary.dictionary.presenation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.lloyds.dictionary.core.util.Resource
import com.lloyds.dictionary.dictionary.data.preferences.UserPreferences
import com.lloyds.dictionary.dictionary.domain.repository.FakeWordInfoRepositoryTest
import com.lloyds.dictionary.dictionary.domain.use_cases.GetWordInfo
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class WordInfoViewModelTest : TestCase() {

    private lateinit var viewModel: WordInfoViewModel
    private lateinit var userPreferences: UserPreferences
    lateinit var getWordInfo: GetWordInfo
    private lateinit var fakeWordInfoRepositoryTest: FakeWordInfoRepositoryTest

    @Before
    override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        userPreferences = UserPreferences(context)
        fakeWordInfoRepositoryTest = FakeWordInfoRepositoryTest()
        getWordInfo = GetWordInfo(fakeWordInfoRepositoryTest)
        viewModel = WordInfoViewModel(getWordInfo, userPreferences)

    }

    @Test
    fun testSuccessSearch() = kotlinx.coroutines.test.runTest {
        val firstItem = getWordInfo("bank").first()
        assertTrue(firstItem.data?.get(0)?.word.equals("bank"))
    }

    @Test
    fun testErrorSearch() = kotlinx.coroutines.test.runTest {

        fakeWordInfoRepositoryTest.wordFlow = flow {
            emit(Resource.Error(data = emptyList(), message = "Unknown Error"))
            delay(3000L)
        }

        val firstItem = getWordInfo("bank").first()
        assertThat(firstItem.message?.equals("Unknown Error"))
    }

    @Test
    fun testLoadingSearch() = kotlinx.coroutines.test.runTest {

        fakeWordInfoRepositoryTest.wordFlow = flow {
            emit(Resource.Loading(data = emptyList()))
            delay(3000L)
        }

        getWordInfo("bank").onEach { result ->
            assertThat(result is Resource.Loading)
        }
    }
}