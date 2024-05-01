package com.hyosik.presentation.ui.intent

import com.hyosik.model.Billboard

data class MainState(
    val isInitialText: Boolean = true,
    val billboard: Billboard,
)
