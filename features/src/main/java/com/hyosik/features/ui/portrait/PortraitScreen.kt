package com.hyosik.features.ui.portrait

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.hyosik.core.ui.state.UiState
import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
import com.hyosik.model.Direction
import com.hyosik.features.extension.orZero
import com.hyosik.features.ui.component.BillBoard
import com.hyosik.features.ui.intent.MainEffect
import com.hyosik.features.ui.intent.MainEvent
import com.hyosik.features.ui.intent.MainState
import com.hyosik.features.ui.theme.buttonText
import com.hyosik.utils.getColor


@Composable
fun PotraitScreen(
    mainState: UiState<MainState>,
    requestOrientationProvider: () -> Unit,
    onColorChanged: (ColorEnvelope) -> Unit,
    onEvent: (MainEvent) -> Unit,
    onSideEffect: (MainEffect) -> Unit
) {

    val controller = rememberColorPickerController()

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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(mainState.isSuccess) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                BillBoard(
                    text = mainState.data?.billboard?.description.orEmpty(), fontSize = mainState.data?.billboard?.fontSize.orZero(),
                    textWidth = { textWidth ->
                        onEvent(MainEvent.SetTextWidth(textWidth = textWidth))
                    },
                    textColor = mainState.data?.billboard?.textColor ?: "FFFFFF",
                    dynamicModifier = getModifier(
                        direction = mainState.data!!.billboard.direction,
                        billboardTextWidth = mainState.data!!.billboard.billboardTextWidth,
                        scrollProvider = { scroll }
                    )
                )

            }

            OutlinedTextField(
                value = mainState.data?.billboard?.description.orEmpty(),
                onValueChange = { newText ->
                    if (newText.length <= maxChar) {
                        mainState.data?.billboard?.let {
                            onEvent(MainEvent.Save(it.copy(
                                key = BILLBOARD_KEY,
                                description = newText,
                            )))
                        }
                    } else {
                        onSideEffect(MainEffect.Toast("최대 길이 입니다!"))
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
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    if (mainState.data?.billboard?.fontSize.orZero() <= maxFontSize) {
                        mainState.data?.billboard?.let {
                            onEvent(MainEvent.Save(
                                it.copy(
                                    fontSize = it.fontSize + 2
                                )
                            ))
                        }
                    }
                    else onSideEffect(MainEffect.Toast("최대 사이즈 입니다!"))
                }) {
                    Text(text = "+", textAlign = TextAlign.Center, fontSize = 25.sp)
                }

                Button(onClick = {
                    requestOrientationProvider()
                }) {
                    Text(text = "START", textAlign = TextAlign.Center, fontSize = 25.sp)
                }

                Button(onClick = {
                    if (mainState.data?.billboard?.fontSize.orZero() >= minFontSize){
                        mainState.data?.billboard?.let {
                            onEvent(MainEvent.Save(
                                it.copy(
                                    fontSize = it.fontSize - 2
                                )
                            ))
                        }

                    }
                    else onSideEffect(MainEffect.Toast("최소 사이즈 입니다!"))
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
                    mainState.data?.billboard?.let {
                        onEvent(MainEvent.Save(
                            it.copy(
                                direction = Direction.LEFT
                            )
                        ))
                    }

                }) {
                    Text(text = "←", textAlign = TextAlign.Center, fontSize = 25.sp)
                }
                Button(onClick = {
                    mainState.data?.billboard?.let {
                        onEvent(
                            MainEvent.Save(
                                it.copy(
                                    direction = Direction.STOP
                                )
                            )
                        )
                    }
                }) {
                    Text(text = "STOP", textAlign = TextAlign.Center, fontSize = 25.sp)
                }
                Button(onClick = {
                    mainState.data?.billboard?.let {
                        onEvent(MainEvent.Save(
                            it.copy(
                                direction = Direction.RIGHT
                            )
                        ))
                    }
                }) {
                    Text(text = "→", textAlign = TextAlign.Center, fontSize = 25.sp)
                }
            }

            HsvColorPicker(
                modifier = Modifier
                    .size(220.dp)
                    .aspectRatio(1f / 1f),
                controller = controller,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                   onColorChanged(colorEnvelope)
                },
                initialColor = if(mainState.data?.billboard?.textColor != null) mainState.data?.billboard?.textColor?.getColor() else null
            )

            //TODO 구글 애드몹 광고 넣기

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
            .graphicsLayer {
                translationX = billboardTextWidth * scrollProvider()
                translationY = 0f
            }


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
            .graphicsLayer {
                translationX = -billboardTextWidth * scrollProvider()
                translationY = 0f
            }
    }
}


@Preview
@Composable
fun PotraitScreenPreview() {
    PotraitScreen(
        mainState = UiState<MainState>(
            data = MainState(
                billboard = Billboard()
            )
        ),
        requestOrientationProvider =  {},
        onColorChanged= {},
        onEvent = {},
        onSideEffect = {}
    )
}