package com.hyosik.model

data class Billboard(
    val key: String = "",
    val description: String = "",
    val fontSize: Int = 100,
    val direction: Direction = Direction.STOP,
    val textColor: String = "FFFFFFFF",
    val billboardTextWidth: Int = 1
)
