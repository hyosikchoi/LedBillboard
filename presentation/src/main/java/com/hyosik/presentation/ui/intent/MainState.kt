package com.hyosik.presentation.ui.intent

import androidx.compose.runtime.Stable
import com.hyosik.model.Billboard

@Stable
data class MainState(
    val isInitialText: Boolean = false,
    val billboard: Billboard,
)
