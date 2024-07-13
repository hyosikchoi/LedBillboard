package com.hyosik.presentation

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.hyosik.presentation.ui.theme.LedBillboardTheme
import com.hyosik.presentation.ui.screen.LandScapeScreen
import com.hyosik.presentation.ui.screen.PotraitScreen
import com.hyosik.presentation.ui.theme.color.Black500
import com.hyosik.presentation.ui.theme.myColorScheme
import dagger.hilt.android.AndroidEntryPoint
import com.hyosik.presentation.ui.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /** orientation 변경 시 HsvColorPicker 에서 OnColorChanged 가 계속 호출 되므로  */
    /** TextColor 가 흰색으로 초기화 되는거 방지용 변수 */
    private var isBackKeyPressed: Boolean = false

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this@MainActivity.window.statusBarColor = Black500.toArgb() // statusBar 색상 설정
        WindowCompat.getInsetsController(this@MainActivity.window, this@MainActivity.window.decorView).apply {
            isAppearanceLightStatusBars = false // statusBar 상단 아이콘 light, dark 설정
        }

        /** onBackPressed deprecated 이 후 BackPressedCallback 이용 */
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로 버튼 이벤트 처리
                if (this@MainActivity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
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
                    color = MaterialTheme.myColorScheme.background
                ) {

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
                                viewModel = hiltViewModel()
                            )
                        }

                        else -> {
                            PotraitScreen(
                                viewModel = hiltViewModel(),
                                requestOrientationProvider = {
                                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                },
                                onColorChanged = { colorEnvelope: ColorEnvelope ->
                                    if (isBackKeyPressed.not()) {
                                        if (colorEnvelope.hexCode != mainViewModel.state.value.data?.billboard?.textColor) {
                                            mainViewModel.state.value.data?.billboard?.let {
                                                mainViewModel.saveBillboard(
                                                    billboard = it.copy(
                                                        textColor = colorEnvelope.hexCode
                                                    )
                                                )
                                            }
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
}

