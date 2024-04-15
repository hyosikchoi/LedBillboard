package com.hyosik.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyosik.presentation.ui.component.BillBoard
import com.hyosik.presentation.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers

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