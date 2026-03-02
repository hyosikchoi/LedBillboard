package com.hyosik.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyosik.theme.myColorScheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    throttleTime: Long = 500,
    enabled: Boolean = true,
    buttonText: String,
    buttonFontSize: TextUnit = 16.sp,
    buttonColor: Color = MaterialTheme.myColorScheme.primary,
    buttonDisableColor: Color = MaterialTheme.myColorScheme.disabledPrimary,
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
        shape = RoundedCornerShape(size = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            disabledContainerColor = buttonDisableColor,
        ),
    ) {
        Text(
            text = buttonText,
            fontSize = buttonFontSize
        )
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(
        modifier = Modifier,
        buttonText = "Primary",
        buttonColor = MaterialTheme.myColorScheme.primary,
        buttonDisableColor = MaterialTheme.myColorScheme.disabledPrimary,
        enabled =true
    ) {}
}