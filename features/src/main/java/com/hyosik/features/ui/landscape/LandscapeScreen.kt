package com.hyosik.features.ui.landscape

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
import androidx.compose.ui.tooling.preview.Preview
import com.hyosik.core.ui.state.UiState
import com.hyosik.model.Billboard
import com.hyosik.model.Direction
import com.hyosik.features.extension.orZero
import com.hyosik.features.ui.component.BillBoard
import com.hyosik.features.ui.intent.MainEvent
import com.hyosik.features.ui.intent.MainState

@Composable
fun LandScapeScreen(
    mainState: UiState<MainState>,
    onEvent: (MainEvent) -> Unit,
) {

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
            text = mainState.data?.billboard?.description.orEmpty(),
            fontSize = mainState.data?.billboard?.fontSize.orZero(),
            textWidth = { textWidth ->
                onEvent(MainEvent.SetTextWidth(textWidth = textWidth))
        },
            textColor = mainState.data?.billboard?.textColor ?: "FFFFFF",
            dynamicModifier = getModifier(
                direction = mainState.data?.billboard?.direction ?: Direction.STOP,
                billboardTextWidth = mainState.data?.billboard?.billboardTextWidth.orZero(),
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
            .graphicsLayer {
                translationX = (billboardTextWidth * scrollProvider())
                translationY = 0f
            }
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
            .graphicsLayer {
                translationX = -billboardTextWidth * scrollProvider()
                translationY = 0f
            }
    }
}


@Preview
@Composable
fun LandScapeScreenPreview() {
    LandScapeScreen(
        mainState = UiState<MainState>(
            data = MainState(
                billboard = Billboard()
            )
        )
    ) {

    }
}