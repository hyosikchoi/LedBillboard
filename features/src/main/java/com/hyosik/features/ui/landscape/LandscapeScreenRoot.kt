package com.hyosik.features.ui.landscape

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyosik.features.ui.viewmodel.MainViewModel

@Composable
fun LandScapeScreenRoot() {

    val mainViewModel: MainViewModel = hiltViewModel()

    // collectAsState vs collectAsStateWithLifecycle
    // UI에서 라이프사이클을 인지하는 방식으로 flow를 수집할 수 있습니다.
    val state by mainViewModel.state.collectAsStateWithLifecycle()

    LandScapeScreen(
        mainState = state,
        onEvent = mainViewModel::onEvent
    )

}

