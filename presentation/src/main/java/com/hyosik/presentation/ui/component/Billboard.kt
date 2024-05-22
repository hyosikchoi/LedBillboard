package com.hyosik.presentation.ui.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.hyosik.presentation.ui.theme.color.Black500

@Composable
fun BillBoard(
    text: String,
    fontSize: Int,
    textWidth: (Int) -> Unit,
    textColor: String,
    dynamicModifier: Modifier,
) {
//    val configuration = LocalConfiguration.current
//    val screenWidthDp: Dp = configuration.screenWidthDp.dp
//    val screenWidthPx = with(LocalDensity.current) { screenWidthDp.toPx() }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            style = MaterialTheme.typography.headlineSmall.copy(
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