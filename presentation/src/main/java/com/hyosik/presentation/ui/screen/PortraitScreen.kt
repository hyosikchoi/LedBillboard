package com.hyosik.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.hyosik.presentation.enum.Direction
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
            singleLine = true
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