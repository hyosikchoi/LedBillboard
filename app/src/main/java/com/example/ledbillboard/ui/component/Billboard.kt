package com.example.ledbillboard.ui.component

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ledbillboard.enum.Direction
import com.example.ledbillboard.ui.theme.*

@Composable
fun BillBoard(
    text: String,
    fontSize: Int,
    direction: Direction,
    modifier: Modifier = Modifier
) {
//    val configuration = LocalConfiguration.current
//    val screenWidthDp: Dp = configuration.screenWidthDp.dp
//    val screenWidthPx = with(LocalDensity.current) { screenWidthDp.toPx() }
    var textWidth: Int by remember { mutableStateOf(1) }

    val infiniteTransition = rememberInfiniteTransition()
    val scroll by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = -1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        )
    )

    val dynamicModifier = when(direction) {
       Direction.LEFT -> {
           modifier
               .fillMaxWidth()
               .horizontalScroll(state = ScrollState(0), enabled = false)
               .graphicsLayer(
                   translationX = (textWidth * scroll),
                   translationY = 0f
               )
        }

       Direction.STOP -> {
           modifier
               .fillMaxWidth()
               .horizontalScroll(state = ScrollState(0), enabled = true)
        }

       Direction.RIGHT -> {
           modifier
               .fillMaxWidth()
               .horizontalScroll(state = ScrollState(0), enabled = false)
               .graphicsLayer(
                   translationX = -textWidth * scroll,
                   translationY = 0f
               )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(color = Black500),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = text,
            modifier = dynamicModifier.onGloballyPositioned { layoutCoordinates ->
                textWidth = layoutCoordinates.size.width
            },
            overflow = TextOverflow.Visible,
            style = MaterialTheme.typography.h4.copy(
                shadow = Shadow(
                    color = Yellow700,
                    offset = Offset(4f, 2f),
                    blurRadius = 50f
                )
            ),
            textAlign = TextAlign.Center,
            fontSize = fontSize.sp,
            color = Yellow700,
            maxLines = 1,
        )
    }

}