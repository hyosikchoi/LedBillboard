package com.hyosik.presentation.ui.intent

import com.hyosik.model.Billboard

sealed class MainEvent {
    open val billboard: Billboard = Billboard(key = "" , description = "")
    data class Initial(
        override val billboard: Billboard
    ) : MainEvent()

    data class Edit(
        override val billboard: Billboard
    ) : MainEvent()
}