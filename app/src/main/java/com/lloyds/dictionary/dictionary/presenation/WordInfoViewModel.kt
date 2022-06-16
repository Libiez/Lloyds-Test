package com.lloyds.dictionary.dictionary.presenation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lloyds.dictionary.core.util.Resource
import com.lloyds.dictionary.dictionary.data.preferences.UserPreferences
import com.lloyds.dictionary.dictionary.domain.use_cases.GetWordInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _state = mutableStateOf(WordInfoState())
    val state: State<WordInfoState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {

            delay(500L)
            getWordInfo(query)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(
                                UIEvent.ShowSnackbar(
                                    result.message ?: "Unknown error"
                                )
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }

                    }
                }.launchIn(this)

        }
    }

    suspend fun isLaunched(): String?  = withContext(Dispatchers.Default){
        userPreferences.isFirstLaunch.first()
    }
    suspend fun saveLaunch(launch:String) {
        userPreferences.saveLaunch(launch)

    }


    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }

}