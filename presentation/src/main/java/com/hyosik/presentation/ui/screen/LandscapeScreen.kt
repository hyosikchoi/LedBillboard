package com.hyosik.presentation.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyosik.model.Direction
import com.hyosik.presentation.ui.component.BillBoard
import com.hyosik.presentation.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun LandScapeScreen(
   viewModel: MainViewModel
) {

    // collectAsState vs collectAsStateWithLifecycle
    // UI에서 라이프사이클을 인지하는 방식으로 flow를 수집할 수 있습니다.
    val cacheState by viewModel.state.collectAsStateWithLifecycle()

    val infiniteTransition = rememberInfiniteTransition()

    /** scroll 값은 지속적으로 변하므로 리컴포지션 조심! */
    val scroll by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = -1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ), label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BillBoard(
            text = cacheState.billboard.description,
            fontSize = cacheState.billboard.fontSize,
            textWidth = { textWidth ->
            viewModel.setTextWidth(textWidth = textWidth)
        },
            textColor = cacheState.billboard.textColor,
            dynamicModifier = getModifier(
                direction = cacheState.billboard.direction,
                billboardTextWidth = cacheState.billboard.billboardTextWidth,
                scrollProvider = {
                    scroll
                }
            )
        )
    }

}

private fun getModifier(
    direction: Direction,
    billboardTextWidth: Int,
    scrollProvider: () -> Float
): Modifier = when (direction) {
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