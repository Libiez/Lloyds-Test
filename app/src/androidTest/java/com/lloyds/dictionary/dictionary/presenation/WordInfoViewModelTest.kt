package com.lloyds.dictionary.dictionary.presenation

import android.content.Context
import android.provider.Contacts
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.base.Predicates.equalTo
import com.google.common.truth.Truth.assertThat
import com.lloyds.dictionary.core.util.Resource
import com.lloyds.dictionary.dictionary.data.preferences.UserPreferences
import com.lloyds.dictionary.dictionary.domain.model.Definition
import com.lloyds.dictionary.dictionary.domain.model.Meaning
import com.lloyds.dictionary.dictionary.domain.model.WordInfo
import com.lloyds.dictionary.dictionary.domain.repository.FakeWordInfoRepositoryTest
import com.lloyds.dictionary.dictionary.domain.use_cases.GetWordInfo
import io.mockk.coEvery
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import retrofit2.HttpException


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class WordInfoViewModelTest: TestCase(){

    lateinit var viewModel: WordInfoViewModel
    lateinit var userPreferences: UserPreferences
    lateinit var getWordInfo: GetWordInfo
    lateinit var fakeWordInfoRepositoryTest: FakeWordInfoRepositoryTest

    @Before
    override fun setUp() {
        super.setUp()
       val context = ApplicationProvider.getApplicationContext<Context>()
       userPreferences = UserPreferences(context)
        fakeWordInfoRepositoryTest= FakeWordInfoRepositoryTest()
       getWordInfo = GetWordInfo(fakeWordInfoRepositoryTest)
       viewModel = WordInfoViewModel(getWordInfo,userPreferences)

    }

    @Test
    fun testSuccessSearch() = kotlinx.coroutines.test.runTest {
        val firstItem = getWordInfo("bank").first()
        assertTrue(firstItem.data?.get(0)?.word.equals("bank"))
    }

    @Test
    fun testErrorSearch() = kotlinx.coroutines.test.runTest {

        fakeWordInfoRepositoryTest.wordFlow= flow {
            emit(Resource.Error(data = emptyList(), message = "Unknown Error"))
            delay(3000L)
        }

        val firstItem = getWordInfo("bank").first()
        assertThat(firstItem.message?.equals("Unknown Error"))
    }

    @Test
    fun testLoadingSearch() = kotlinx.coroutines.test.runTest {

        fakeWordInfoRepositoryTest.wordFlow= flow {
            emit(Resource.Loading(data = emptyList()))
            delay(3000L)
        }

        getWordInfo("bank").onEach { result->
            assertThat(result is Resource.Loading)
        }

    }



}