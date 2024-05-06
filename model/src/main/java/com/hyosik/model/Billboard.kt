package com.hyosik.model

data class Billboard(
    val key: String,
    val description: String,
    val fontSize: Int,
    val direction: Direction,
    val textColor: String,
    val billboardTextWidth: Int
)
