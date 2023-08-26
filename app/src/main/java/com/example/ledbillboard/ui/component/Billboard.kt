package com.example.ledbillboard.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BillBoard(text: String, fontSize: Int) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .wrapContentHeight(align = Alignment.CenterVertically),
        text = text,
        textAlign = TextAlign.Center,
        fontSize = fontSize.sp
    )
}