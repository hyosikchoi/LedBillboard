package com.hyosik.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.hyosik.presentation.enum.Direction
import com.hyosik.presentation.ui.component.BillBoard
import com.hyosik.presentation.ui.viewmodel.MainViewModel

@Composable
fun PotraitScreen(
    viewModel: MainViewModel,
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

    // collectAsState vs collectAsStateWithLifecycle
    // UI에서 라이프사이클을 인지하는 방식으로 flow를 수집할 수 있습니다.
    val cacheBillboard by viewModel.billboardState.collectAsStateWithLifecycle()

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
                text = cacheBillboard.description, fontSize = fontSize, textWidth = { textWidth ->
                    textWidthProvider(textWidth)
                },
                textColor = textColor,
                dynamicModifier = dynamicModifier
            )
        }
        //TODO 세팅 하고나서 커서 젤 뒤로 이동 추가
        OutlinedTextField(
            value = cacheBillboard.description,
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