package com.hyosik.features.ui.theme.color

import android.graphics.Color.parseColor
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val White200 = Color(parseColor("#FFFFFF"))
val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Black200 = Color(0xFF333232)
val Black500 = Color(0xFF1D1D1D)
val Black700 = Color(0xFF141414)
val Yellow200 = Color(0xFFF8E962)
val Yellow500 = Color(0xFFF8E534)
val Yellow700 = Color(0xFFF8E114)
val Red200 =  Color(0xFFF84545)
val Red500 =  Color(0xFFF82222)
val Red700 =  Color(0xFFF80E0E)


sealed class ColorSet {

    open lateinit var myColors: MyColors
    object LightColorSet: ColorSet() {
        override var myColors = MyColors(
            material = lightColorScheme(
                primary = Purple500,
                primaryContainer = Purple700,
                secondary = Teal200,
            ),
            text1 = Black200
        )
    }

    object DarkColorSet: ColorSet() {
        override var myColors = MyColors(
            material = darkColorScheme(
                primary = Purple200,
                primaryContainer = Purple200,
                secondary = Teal200,
            ),
            text1 = White200
        )
    }

}