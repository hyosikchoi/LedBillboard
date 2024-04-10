package com.hyosik.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hyosik.presentation.ui.component.BillBoard

@Composable
fun LandScapeScreen(
    text: String,
    fontSize: Int,
    textWidthProvider: (Int) -> Unit,
    textColor: String,
    dynamicModifier: Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BillBoard(text = text, fontSize = fontSize, textWidth = { textWidth ->
            textWidthProvider(textWidth)
        },
            textColor = textColor,
            dynamicModifier = dynamicModifier
        )
    }

}