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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
import com.hyosik.model.Direction
import com.hyosik.presentation.enum.ToastType
import com.hyosik.presentation.extension.toast
import com.hyosik.presentation.ui.component.BillBoard
import com.hyosik.presentation.ui.viewmodel.MainViewModel

@Composable
fun PotraitScreen(
    viewModel: MainViewModel,
    requestOrientationProvider: () -> Unit,
    onColorChanged: (ColorEnvelope) -> Unit
) {

    val context = LocalContext.current

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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            BillBoard(
                text = cacheState.billboard.description, fontSize = cacheState.billboard.fontSize, textWidth = { textWidth ->
                    viewModel.setTextWidth(textWidth = textWidth)
                },
                textColor = cacheState.billboard.textColor,
                dynamicModifier = getModifier(
                    direction = cacheState.billboard.direction,
                    billboardTextWidth = cacheState.billboard.billboardTextWidth,
                    scrollProvider = { scroll }
                )
            )
        }
        OutlinedTextField(
            value = cacheState.billboard.description,
            onValueChange = { newText ->
                if (newText.length <= maxChar) {
                    viewModel.saveBillboard(
                        cacheState.billboard.copy(
                            key = BILLBOARD_KEY,
                            description = newText,
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            singleLine = true,

        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                if (cacheState.billboard.fontSize <= maxFontSize) {
                    viewModel.saveBillboard(
                        cacheState.billboard.copy(
                            fontSize = cacheState.billboard.fontSize + 2
                        )
                    )
                }
                else context.toast("최대 사이즈 입니다.", ToastType.SHORT)
            }) {
                Text(text = "+", textAlign = TextAlign.Center, fontSize = 25.sp)
            }

            Button(onClick = {
                requestOrientationProvider()
            }) {
                Text(text = "START", textAlign = TextAlign.Center, fontSize = 25.sp)
            }

            Button(onClick = {
                if (cacheState.billboard.fontSize >= minFontSize){
                    viewModel.saveBillboard(
                        cacheState.billboard.copy(
                            fontSize = cacheState.billboard.fontSize - 2
                        )
                    )
                }
                else context.toast("최소 사이즈 입니다.", ToastType.SHORT)
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
                viewModel.saveBillboard(
                    cacheState.billboard.copy(
                        direction = Direction.LEFT
                    )
                )
            }) {
                Text(text = "←", textAlign = TextAlign.Center, fontSize = 25.sp)
            }
            Button(onClick = {
                viewModel.saveBillboard(
                    cacheState.billboard.copy(
                        direction = Direction.STOP
                    )
                )
            }) {
                Text(text = "STOP", textAlign = TextAlign.Center, fontSize = 25.sp)
            }
            Button(onClick = {
                viewModel.saveBillboard(
                    cacheState.billboard.copy(
                        direction = Direction.RIGHT
                    )
                )
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
                if(cacheState.isInitialText) onColorChanged(colorEnvelope)
            },
        )
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