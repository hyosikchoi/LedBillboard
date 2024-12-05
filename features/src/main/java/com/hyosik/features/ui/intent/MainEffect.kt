package com.hyosik.features.ui.intent

import androidx.compose.runtime.Stable

@Stable
sealed class MainEffect {
    data class Toast(
        val msg: String
    ) : MainEffect()
}