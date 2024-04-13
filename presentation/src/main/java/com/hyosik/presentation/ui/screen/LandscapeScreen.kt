package com.hyosik.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyosik.presentation.ui.component.BillBoard
import com.hyosik.presentation.ui.viewmodel.MainViewModel

@Composable
fun LandScapeScreen(
    viewModel: MainViewModel,
    fontSize: Int,
    textWidthProvider: (Int) -> Unit,
    textColor: String,
    dynamicModifier: Modifier
) {

    val cacheBillboard by viewModel.billboardState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BillBoard(text = cacheBillboard.description, fontSize = fontSize, textWidth = { textWidth ->
            textWidthProvider(textWidth)
        },
            textColor = textColor,
            dynamicModifier = dynamicModifier
        )
    }

}