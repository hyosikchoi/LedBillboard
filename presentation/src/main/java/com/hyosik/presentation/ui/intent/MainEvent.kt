package com.hyosik.presentation.ui.intent

import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
import com.hyosik.model.Direction

sealed class MainEvent {
    open val billboard: Billboard = Billboard(
        key = BILLBOARD_KEY,
        description = "",
        fontSize = 100,
        direction = Direction.STOP,
        textColor = "FFFFFFFF",
        billboardTextWidth = 1
    )

    data class Initial(
        override val billboard: Billboard
    ) : MainEvent()

    data class Edit(
        override val billboard: Billboard
    ) : MainEvent()
}