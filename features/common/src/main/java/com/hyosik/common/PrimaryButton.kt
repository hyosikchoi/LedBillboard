package com.hyosik.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PrimaryButton(
    modifier: Modifier,
    throttleTime: Long = 500,
    enabled: Boolean = true,
    buttonText: String,
    onClick: () -> Unit
) {

    val lastClickTimestamp = remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Button(
        modifier = modifier,
        onClick = {
            val currentTimestamp = System.currentTimeMillis()
            if (currentTimestamp - lastClickTimestamp.value >= throttleTime) {
                coroutineScope.launch {
                    withContext(Dispatchers.Main) {
                        onClick()
                    }
                }
                lastClickTimestamp.value = currentTimestamp
            }
        },
        enabled = enabled,
        shape = RoundedCornerShape(size = 6.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
    ) {
        Text(
            text = buttonText
        )
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(
        modifier = Modifier,
        buttonText = "Primary"
    ) {}
}