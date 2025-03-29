package com.hyosik.extensions

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun Modifier.throttleClickable(
    throttleTime: Long = 300,
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed {
    val lastClickTimestamp = remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
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
        }
    )
}