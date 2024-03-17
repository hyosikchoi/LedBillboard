package com.example.ledbillboard

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ledbillboard.ui.component.BillBoard
import com.example.ledbillboard.ui.theme.LedBillboardTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
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

    /** orientation 변경 시 HsvColorPicker 에서 OnColorChanged 가 계속 호출 되므로  */
    /** TextColor 가 흰색으로 초기화 되는거 방지용 변수 */
    private var isBackKeyPressed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       /** onBackPressed deprecated 이 후 BackPressedCallback 이용 */
       val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로 버튼 이벤트 처리
                if(this@MainActivity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    finish()
                } else {
                    isBackKeyPressed = true
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
        }

        this.onBackPressedDispatcher.addCallback(this, callback)

        setContent {
            LedBillboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    var text: String by rememberSaveable { mutableStateOf("") }

                    var fontSize: Int by rememberSaveable { mutableStateOf(100) }

                    var direction: Direction by rememberSaveable { mutableStateOf(Direction.STOP) }

                    val controller = rememberColorPickerController()

                    var textColor: String by rememberSaveable { mutableStateOf("FFFFFFFF") }

                    val maxChar: Int = 30

                    val minFontSize: Int = 60

                    val maxFontSize: Int = 140

                    var billboardTextWidth: Int by rememberSaveable { mutableStateOf(1) }

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

                    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }

                    val configuration = LocalConfiguration.current

                    LaunchedEffect(configuration) {
                        orientation = configuration.orientation
//                        // Save any changes to the orientation value on the configuration object
//                        snapshotFlow { configuration.orientation }
//                            .collect { orientation = it }
                    }

                    when (orientation) {
                        Configuration.ORIENTATION_LANDSCAPE -> {
                            //TODO Landscape Screen 따로 생성
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                BillBoard(text = text, fontSize = fontSize, textWidth = { textWidth ->
                                    if(textWidth != billboardTextWidth) billboardTextWidth = textWidth
                                },
                                    textColor = textColor,
                                    dynamicModifier = dynamicModifier
                                )
                            }
                        }
                        else -> {
                            //TODO Portrait Screen 따로 생성
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                        .height(250.dp)
                                ) {
                                    BillBoard(text = text, fontSize = fontSize, textWidth = { textWidth ->
                                        if(textWidth != billboardTextWidth) billboardTextWidth = textWidth
                                    },
                                        textColor = textColor,
                                        dynamicModifier = dynamicModifier
                                    )
                                }
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
                                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                    }) {
                                        Text(text = "START", textAlign = TextAlign.Center, fontSize = 25.sp)
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
                                        if(isBackKeyPressed.not()) {
                                            if(colorEnvelope.hexCode != textColor) {
                                                textColor = colorEnvelope.hexCode
                                            }
                                        } else {
                                           isBackKeyPressed = false
                                        }
                                    },
                                )
                            }
                        }
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

