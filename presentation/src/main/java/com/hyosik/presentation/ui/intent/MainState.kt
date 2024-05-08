package com.hyosik.presentation.ui.intent

import com.hyosik.model.Billboard

data class MainState(
    val isInitialText: Boolean = false,
    val billboard: Billboard,
)
