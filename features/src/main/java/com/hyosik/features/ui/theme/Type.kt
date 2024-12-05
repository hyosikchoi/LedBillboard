package com.hyosik.features.ui.theme


import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ledbillboard.presentation.R


private val spoqaRegular = FontFamily(
    Font(R.font.spoqa_han_sans_neo_bold, FontWeight.Normal)
)

private val spoqaBold = FontFamily(
    Font(R.font.spoqa_han_sans_neo_bold, FontWeight.Bold)
)

private val spoqaThin = FontFamily(
    Font(R.font.spoqa_han_sans_neo_bold, FontWeight.Thin)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    titleSmall = TextStyle(
        fontFamily = spoqaRegular,
        fontSize = 15.sp
    )
)

val Typography.buttonText: TextStyle
    @Composable get() = titleSmall.copy(
        fontSize = 20.sp
    )