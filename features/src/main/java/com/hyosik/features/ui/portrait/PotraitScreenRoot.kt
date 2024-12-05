package com.hyosik.features.ui.portrait

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.hyosik.features.enum.ToastType
import com.hyosik.features.extension.toast
import com.hyosik.features.ui.intent.MainEffect
import com.hyosik.features.ui.viewmodel.MainViewModel
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
                when (sideEffect) {
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
        onColorChanged = { colorEnvelope: ColorEnvelope ->
          if(state.data?.isInitialText == true) onColorChanged(colorEnvelope)
        },
        onEvent = mainViewModel::onEvent,
        onSideEffect = mainViewModel::sendSideEffect
    )


}