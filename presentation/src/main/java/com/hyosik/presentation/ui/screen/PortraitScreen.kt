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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.hyosik.model.Direction
import com.hyosik.presentation.ui.component.BillBoard

@Composable
fun PotraitScreen(
    text: String,
    fontSize: Int,
    textWidthProvider: (Int) -> Unit,
    textColor: String,
    dynamicModifier: Modifier,
    requestOrientationProvider: () -> Unit,
    onValueChange: (String) -> Unit,
    fontSizeUp: () -> Unit,
    fontSizeDown: () -> Unit,
    directionProvider: (Direction) -> Unit,
    onColorChanged: (ColorEnvelope) -> Unit
) {

    val controller = rememberColorPickerController()

    val infiniteTransition = rememberInfiniteTransition()

    val maxChar: Int = 30

    val minFontSize: Int = 60

    val maxFontSize: Int = 140

//    /** scroll 값은 지속적으로 변하므로 리컴포지션 조심! */
//    val scroll by infiniteTransition.animateFloat(
//        initialValue = 1f,
//        targetValue = -1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(10000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart,
//        ), label = ""
//    )

//    /** scroll 값 때문에 리컴포지션 방지하기 위해 scrollProvider 람다식으로 변경! */
//    val dynamicModifier = getModifier(
//        direction = cacheState.billboard.direction,
//        billboardTextWidth = cacheState.billboard.billboardTextWidth,
//        scrollProvider = { scroll }
//    )

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
                text = text, fontSize = fontSize, textWidth = { textWidth ->
                    textWidthProvider(textWidth)
                },
                textColor = textColor,
                dynamicModifier = dynamicModifier
            )
        }
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                onValueChange(newText)
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
                fontSizeUp()
            }) {
                Text(text = "+", textAlign = TextAlign.Center, fontSize = 25.sp)
            }

            Button(onClick = {
                requestOrientationProvider()
            }) {
                Text(text = "START", textAlign = TextAlign.Center, fontSize = 25.sp)
            }

            Button(onClick = {
                fontSizeDown()
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
            Button(onClick = { directionProvider(Direction.LEFT) }) {
                Text(text = "←", textAlign = TextAlign.Center, fontSize = 25.sp)
            }
            Button(onClick = { directionProvider(Direction.STOP) }) {
                Text(text = "STOP", textAlign = TextAlign.Center, fontSize = 25.sp)
            }
            Button(onClick = { directionProvider(Direction.RIGHT) }) {
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
                onColorChanged(colorEnvelope)
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