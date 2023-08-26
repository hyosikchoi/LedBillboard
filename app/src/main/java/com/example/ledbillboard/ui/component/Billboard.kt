package com.example.ledbillboard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ledbillboard.ui.theme.*

@Composable
fun BillBoard(text: String, fontSize: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(color = Black500)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(align = Alignment.CenterVertically),
            text = text,
            style = MaterialTheme.typography.h4.copy(
                shadow = Shadow(
                    color = Yellow700,
                    offset = Offset(4f, 2f),
                    blurRadius = 50f
                )
            ),
            textAlign = TextAlign.Center,
            fontSize = fontSize.sp,
            color = Yellow700
        )
    }

}