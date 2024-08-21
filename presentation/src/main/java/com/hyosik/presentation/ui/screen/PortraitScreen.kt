package com.hyosik.presentation.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
import com.hyosik.model.Direction
import com.hyosik.presentation.enum.ToastType
import com.hyosik.presentation.extension.orZero
import com.hyosik.presentation.extension.toast
import com.hyosik.presentation.ui.component.BillBoard
import com.hyosik.presentation.ui.intent.MainEffect
import com.hyosik.presentation.ui.theme.buttonText
import com.hyosik.presentation.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
//TODO viewModel 관련해서 Wrapping 하는 Composable 을 만들고 state 를 주입 하게끔 설계 변경
@Composable
fun PotraitScreen(
    viewModel: MainViewModel,
    requestOrientationProvider: () -> Unit,
    onColorChanged: (ColorEnvelope) -> Unit
) {

    val context = LocalContext.current

    val lifeCycleOwner = LocalLifecycleOwner.current

    val controller = rememberColorPickerController()

    // collectAsState vs collectAsStateWithLifecycle
    // UI에서 라이프사이클을 인지하는 방식으로 flow를 수집할 수 있습니다.
    val cacheState by viewModel.state.collectAsStateWithLifecycle()

    val infiniteTransition = rememberInfiniteTransition()

    val maxChar: Int = 30

    val minFontSize: Int = 60

    val maxFontSize: Int = 140

    /** scroll 값은 지속적으로 변하므로 리컴포지션 조심! */
    val scroll by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = -1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ), label = ""
    )

    LaunchedEffect(Unit) {
        lifeCycleOwner.lifecycleScope.launch {
            viewModel.sideEffects.collect { sideEffect ->
                when(sideEffect) {
                    is MainEffect.Toast -> {
                        context.toast(sideEffect.msg, ToastType.SHORT)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(cacheState.isSuccess) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                BillBoard(
                    text = cacheState.data?.billboard?.description.orEmpty(), fontSize = cacheState.data?.billboard?.fontSize.orZero(),
                    textWidth = { textWidth ->
                        viewModel.setTextWidth(textWidth = textWidth)
                    },
                    textColor = cacheState.data!!.billboard.textColor,
                    dynamicModifier = getModifier(
                        direction = cacheState.data!!.billboard.direction,
                        billboardTextWidth = cacheState.data!!.billboard.billboardTextWidth,
                        scrollProvider = { scroll }
                    )
                )
            }

            //TODO 리컴포지션의 영향이 현재 value 에 매개변수로 cacheState 의 description 을 넘겨 주고 있어서 그렇다. 수정해야함.
            OutlinedTextField(
                value = cacheState.data?.billboard?.description.orEmpty(),
                onValueChange = { newText ->
                    if (newText.length <= maxChar) {
                        cacheState.data?.billboard?.let {
                            viewModel.saveBillboard(
                                it.copy(
                                    key = BILLBOARD_KEY,
                                    description = newText,
                                )
                            )
                        }
                    } else {
                        viewModel.sendSideEffect(effect = MainEffect.Toast("최대 길이 입니다!"))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                singleLine = true,
                textStyle = MaterialTheme.typography.buttonText
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    if (cacheState.data?.billboard?.fontSize.orZero() <= maxFontSize) {
                        cacheState.data?.billboard?.let {
                            viewModel.saveBillboard(
                                it.copy(
                                    fontSize = it.fontSize + 2
                                )
                            )
                        }
                    }
                    else viewModel.sendSideEffect(effect = MainEffect.Toast("최대 사이즈 입니다!"))
                }) {
                    Text(text = "+", textAlign = TextAlign.Center, fontSize = 25.sp)
                }

                Button(onClick = {
                    requestOrientationProvider()
                }) {
                    Text(text = "START", textAlign = TextAlign.Center, fontSize = 25.sp)
                }

                Button(onClick = {
                    if (cacheState.data?.billboard?.fontSize.orZero() >= minFontSize){
                        cacheState.data?.billboard?.let {
                            viewModel.saveBillboard(
                                it.copy(
                                    fontSize = it.fontSize - 2
                                )
                            )
                        }

                    }
                    else viewModel.sendSideEffect(effect = MainEffect.Toast("최소 사이즈 입니다!"))
                }) {
                    Text(text = "-", textAlign = TextAlign.Center, fontSize = 25.sp)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    cacheState.data?.billboard?.let {
                        viewModel.saveBillboard(
                            it.copy(
                                direction = Direction.LEFT
                            )
                        )
                    }

                }) {
                    Text(text = "←", textAlign = TextAlign.Center, fontSize = 25.sp)
                }
                Button(onClick = {
                    cacheState.data?.billboard?.let {
                        viewModel.saveBillboard(
                            it.copy(
                                direction = Direction.STOP
                            )
                        )
                    }
                }) {
                    Text(text = "STOP", textAlign = TextAlign.Center, fontSize = 25.sp)
                }
                Button(onClick = {
                    cacheState.data?.billboard?.let {
                        viewModel.saveBillboard(
                            it.copy(
                                direction = Direction.RIGHT
                            )
                        )
                    }
                }) {
                    Text(text = "→", textAlign = TextAlign.Center, fontSize = 25.sp)
                }
            }

            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(10.dp),
                controller = controller,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    if(cacheState.data?.isInitialText == true) onColorChanged(colorEnvelope)
                },
            )     
        }
    }
}

private fun getModifier(
    direction: Direction,
    billboardTextWidth: Int,
    scrollProvider: () -> Float
): Modifier = when (direction) {
    Direction.LEFT -> {
        Modifier
            .fillMaxWidth()
            .horizontalScroll(state = ScrollState(0), enabled = false)
            .graphicsLayer(
                translationX = (billboardTextWidth * scrollProvider()),
                translationY = 0f
            )
    }

    Direction.STOP -> {
        Modifier
            .fillMaxWidth()
            .horizontalScroll(state = ScrollState(0), enabled = true)
    }

    Direction.RIGHT -> {
        Modifier
            .fillMaxWidth()
            .horizontalScroll(state = ScrollState(0), enabled = false)
            .graphicsLayer(
                translationX = -billboardTextWidth * scrollProvider(),
                translationY = 0f
            )
    }
}