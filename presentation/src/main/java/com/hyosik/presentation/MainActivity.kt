package com.hyosik.presentation

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.hyosik.presentation.ui.theme.LedBillboardTheme
import com.hyosik.presentation.enum.Direction
import com.hyosik.presentation.enum.ToastType
import com.hyosik.presentation.extension.toast
import com.hyosik.presentation.ui.screen.LandScapeScreen
import com.hyosik.presentation.ui.screen.PotraitScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                    color = MaterialTheme.colorScheme.background
                ) {

                    var text: String by rememberSaveable { mutableStateOf("") }

                    var fontSize: Int by rememberSaveable { mutableStateOf(100) }

                    var direction: Direction by rememberSaveable { mutableStateOf(Direction.STOP) }

                    var textColor: String by rememberSaveable { mutableStateOf("FFFFFFFF") }

                    var billboardTextWidth: Int by rememberSaveable { mutableStateOf(1) }

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
                            LandScapeScreen(
                                text = text,
                                fontSize = fontSize,
                                textColor = textColor,
                                dynamicModifier = dynamicModifier,
                                textWidthProvider = { textWidth ->
                                    if(textWidth != billboardTextWidth) billboardTextWidth = textWidth
                                }
                            )
                        }
                        else -> {
                            PotraitScreen(
                                text = text,
                                fontSize = fontSize,
                                textColor = textColor,
                                dynamicModifier = dynamicModifier,
                                textWidthProvider = { textWidth ->
                                    if(textWidth != billboardTextWidth) billboardTextWidth = textWidth
                                },
                                onValueChange = { newText ->
                                    if(newText.length <= maxChar) text = newText
                                },
                                fontSizeUp = {
                                    if(fontSize <= maxFontSize) fontSize += 2
                                    else this@MainActivity.toast("최대 사이즈 입니다.", ToastType.SHORT)
                                },
                                fontSizeDown = {
                                    if(fontSize >= minFontSize) fontSize -= 2
                                    else this@MainActivity.toast("최소 사이즈 입니다.", ToastType.SHORT)
                                },
                                requestOrientationProvider = {
                                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                },
                                directionProvider = { changeDirection: Direction ->
                                    direction = changeDirection
                                },
                                onColorChanged = { colorEnvelope: ColorEnvelope ->
                                    if (isBackKeyPressed.not()) {
                                        if (colorEnvelope.hexCode != textColor) {
                                            textColor = colorEnvelope.hexCode
                                        }
                                    } else {
                                        isBackKeyPressed = false
                                    }
                                }
                            )
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

