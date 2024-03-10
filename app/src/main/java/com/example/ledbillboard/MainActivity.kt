package com.example.ledbillboard

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ledbillboard.ui.component.BillBoard
import com.example.ledbillboard.ui.theme.LedBillboardTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ledbillboard.enum.Direction
import com.example.ledbillboard.enum.ToastType
import com.example.ledbillboard.extension.toast
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LedBillboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        var text: String by rememberSaveable { mutableStateOf("") }

                        var fontSize: Int by remember { mutableStateOf(100) }

                        var direction: Direction by remember { mutableStateOf(Direction.STOP) }

                        val controller = rememberColorPickerController()

                        var textColor: String by remember { mutableStateOf("FFFFFFFF") }

                        val maxChar: Int = 30

                        val minFontSize: Int = 60

                        val maxFontSize: Int = 140

                        var billboardTextWidth: Int by remember { mutableStateOf(1) }

                        val infiniteTransition = rememberInfiniteTransition()

                        /** scroll 값은 지속적으로 변하므로 리컴포지션 조심! */
                        val scroll by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = -1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(10000, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart,
                            )
                        )
                        /** scroll 값 때문에 리컴포지션 방지하기 위해 scrollProvider 람다식으로 변경! */
                        val dynamicModifier = getModifier(direction = direction, billboardTextWidth = billboardTextWidth, scrollProvider = { scroll })

                        BillBoard(text = text, fontSize = fontSize, textWidth = { textWidth ->
                            if(textWidth != billboardTextWidth) billboardTextWidth = textWidth
                        },
                        textColor = textColor,
                        dynamicModifier = dynamicModifier
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { newText ->
                                if(newText.length <= maxChar) text = newText
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
                                if(fontSize <= maxFontSize) fontSize += 2
                                else this@MainActivity.toast("최대 사이즈 입니다.", ToastType.SHORT)
                            }) {
                                Text(text = "+", textAlign = TextAlign.Center, fontSize = 25.sp)
                            }
                            Button(onClick = {
                                if(fontSize >= minFontSize) fontSize -= 2
                                else this@MainActivity.toast("최소 사이즈 입니다.", ToastType.SHORT)
                            }) {
                                Text(text = "-", textAlign = TextAlign.Center, fontSize = 25.sp)
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                            ,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = { direction = Direction.LEFT }) {
                                Text(text = "←", textAlign = TextAlign.Center, fontSize = 25.sp)
                            }
                            Button(onClick = { direction = Direction.STOP }) {
                                Text(text = "STOP", textAlign = TextAlign.Center, fontSize = 25.sp)
                            }
                            Button(onClick = { direction = Direction.RIGHT }) {
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
                                // do something
                                if(colorEnvelope.hexCode != textColor) {
                                    textColor = colorEnvelope.hexCode
                                }
                            }
                        )

                    }
                }
            }
        }
    }

    private fun getModifier(direction: Direction, billboardTextWidth: Int, scrollProvider: () -> Float): Modifier = when(direction) {
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

}

