package com.hyosik.features.ui.intent

import androidx.compose.runtime.Stable
import com.hyosik.common.Effect

@Stable
sealed class MainEffect : Effect {
    data class Toast(
        val msg: String
    ) : MainEffect()
}