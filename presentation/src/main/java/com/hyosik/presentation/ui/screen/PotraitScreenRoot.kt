package com.hyosik.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.hyosik.presentation.enum.ToastType
import com.hyosik.presentation.extension.toast
import com.hyosik.presentation.ui.intent.MainEffect
import com.hyosik.presentation.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun PotraitScreenRoot(
    requestOrientationProvider: () -> Unit,
    onColorChanged: (ColorEnvelope) -> Unit
) {

    val mainViewModel: MainViewModel = hiltViewModel()

    val state by mainViewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifeCycleOwner.lifecycleScope.launch {
            mainViewModel.sideEffects.collect { sideEffect ->
                when(sideEffect) {
                    is MainEffect.Toast -> {
                        context.toast(sideEffect.msg, ToastType.SHORT)
                    }
                }
            }
        }
    }

    PotraitScreen(
        mainState = state,
        requestOrientationProvider = { requestOrientationProvider() },
        onColorChanged = { colorEnvelope: ColorEnvelope ->  onColorChanged(colorEnvelope) },
        onEvent = mainViewModel::onEvent,
        onSideEffect = mainViewModel::sendSideEffect
    )


}