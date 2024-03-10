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
import androidx.compose.ui.graphics.Color
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
    textWidth: (Int) -> Unit,
    textColor: String,
    dynamicModifier: Modifier
) {
//    val configuration = LocalConfiguration.current
//    val screenWidthDp: Dp = configuration.screenWidthDp.dp
//    val screenWidthPx = with(LocalDensity.current) { screenWidthDp.toPx() }

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
                textWidth(layoutCoordinates.size.width)
            },
            overflow = TextOverflow.Visible,
            style = MaterialTheme.typography.h4.copy(
                shadow = Shadow(
                    color = Color(android.graphics.Color.parseColor("#${textColor}")),
                    offset = Offset(4f, 2f),
                    blurRadius = 50f
                )
            ),
            textAlign = TextAlign.Center,
            fontSize = fontSize.sp,
            color = Color(android.graphics.Color.parseColor("#${textColor}")),
            maxLines = 1,
        )
    }

}