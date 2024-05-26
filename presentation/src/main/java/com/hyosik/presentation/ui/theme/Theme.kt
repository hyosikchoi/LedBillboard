package com.hyosik.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hyosik.presentation.ui.theme.color.Black500
import com.hyosik.presentation.ui.theme.color.ColorSet
import com.hyosik.presentation.ui.theme.color.MyColors

private val LocalColors = staticCompositionLocalOf { ColorSet.DarkColorSet.myColors } // 초기값 설정

@Composable
fun LedBillboardTheme(
    typegraphy: Typography = Typography,
    shapes: Shapes = Shapes,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    /** 상단 SystemBar Color 변경 */
    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(
        color = Black500
    )

    val colors: MyColors = if (darkTheme) {
        ColorSet.DarkColorSet.myColors
    } else {
        ColorSet.LightColorSet.myColors
    }

    // provides 로 colors 변수를 LocalColors 에 주입
    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            colorScheme = colors.material,
            typography = typegraphy,
            shapes = shapes,
            content = content
        )
    }
}

/**
 * 현재 내 테마에 적용된 색깔을 확장함수 형태로 만들어 어디서든 read 하여 사용할 수 있게 한다.
 */
val MaterialTheme.myColorScheme: MyColors
@Composable
@ReadOnlyComposable
get() = LocalColors.current

