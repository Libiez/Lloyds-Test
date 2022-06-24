package com.lloyds.dictionary.dictionary.presenation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lloyds.dictionary.MainActivity
import com.lloyds.dictionary.MainActivity.Companion.SEARCH_LOADER
import com.lloyds.dictionary.MainActivity.Companion.SEARCH_TEXTFIELD
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreens() {
    val viewModel: WordInfoViewModel = hiltViewModel()


    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {

        viewModel.eventFlow.collectLatest { event ->

            when (event) {
                is WordInfoViewModel.UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )

                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {

        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                TextField(
                    value = viewModel.searchQuery.value,
                    onValueChange = viewModel::onSearch,
                    modifier = Modifier.testTag(SEARCH_TEXTFIELD).fillMaxWidth(),
                    placeholder = {
                        Text(text = "Search...",fontFamily = FontFamily.Monospace)
                    })

                AnimatedVisibility(visible = viewModel.state.value.isLoading, modifier = Modifier.testTag(SEARCH_LOADER)) {

                    Column(modifier = Modifier.fillMaxWidth()) {

                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                                .height(4.dp),
                            backgroundColor = Color.LightGray,
                            color = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    items(state.wordInfoItems.size) { index ->

                        val wordInfo = state.wordInfoItems[index]
                        if (index > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        WordInfoItem(wordInfo = wordInfo)
                        if (index < state.wordInfoItems.size - 1) {
                            Divider()
                        }

                    }

                }

            }


        }
    }
}
