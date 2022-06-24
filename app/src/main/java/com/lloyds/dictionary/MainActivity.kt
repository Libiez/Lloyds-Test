package com.lloyds.dictionary

import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lloyds.dictionary.dictionary.presenation.WordInfoItem
import com.lloyds.dictionary.dictionary.presenation.WordInfoViewModel
import com.lloyds.dictionary.ui.theme.DictionaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {

        var SPLASH_SCREEN = "splash_screen"
        var SEARCH_SCREEN = "search_screen"
        var FIRST_LAUNCH = "first_launch"
        var LAUNCHED = "launched"
        var SEARCH_TESTIFIED = "searchTextField"
        var SEARCH_LOADER = "searchLoader"
    }

    private lateinit var viewModel: WordInfoViewModel
    private var isLaunched: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel = hiltViewModel()
            DictionaryTheme {
                Navigation()
            }
        }

    }

    @Composable
    fun Navigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = SPLASH_SCREEN) {

            viewModel.viewModelScope.launch {
                isLaunched = viewModel.isLaunched()
            }

            composable(SPLASH_SCREEN) {
                Splashscreen(navController)
            }

            composable(SEARCH_SCREEN) {
                SearchScreen()
                BackHandler(true) {
                    finish()
                }
            }

        }
    }


    @Composable
    fun Splashscreen(navController: NavController? = null) {
        val scale = remember {
            androidx.compose.animation.core.Animatable(0f)
        }
        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 0.3f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = {
                        OvershootInterpolator(2f).getInterpolation(it)
                    }
                )
            )

            delay(300L)
            navController?.navigate(SEARCH_SCREEN)

        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            Row {
                Image(
                    painter = painterResource(id = R.drawable.auto_stories),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.padding(8.dp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 20.sp
                )
            }

            if (isLaunched.isNullOrEmpty()) {

                viewModel.viewModelScope.launch {
                    viewModel.saveLaunch(FIRST_LAUNCH)
                }

                Text(
                    text = stringResource(id = R.string.intro_text),
                    modifier = Modifier
                        .padding(top = 200.dp)
                        .padding(16.dp),
                    textAlign = TextAlign.Center, fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 25.sp
                )
            } else if (isLaunched.equals(FIRST_LAUNCH)) {

                viewModel.viewModelScope.launch {
                    viewModel.saveLaunch(LAUNCHED)
                }

            }

        }
    }

    @Composable
    fun SearchScreen() {

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
                        modifier = Modifier
                            .testTag(SEARCH_TESTIFIED)
                            .fillMaxWidth(),
                        placeholder = {
                            Text(text = "Search...", fontFamily = FontFamily.Monospace)
                        })

                    AnimatedVisibility(
                        visible = viewModel.state.value.isLoading,
                        modifier = Modifier.testTag(SEARCH_LOADER)
                    ) {

                        Column(modifier = Modifier.fillMaxWidth()) {

                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
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

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview3() {

        Surface(modifier = Modifier.fillMaxSize()) {
            Splashscreen()
        }
    }
}


