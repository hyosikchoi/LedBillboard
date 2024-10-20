package com.hyosik.utils

import androidx.compose.ui.graphics.Color

fun String.getColor(): Color =
    when(this.length) {
        8 -> {
            // AARRGGBB 형식일 경우
            val alpha = this.substring(0, 2).toInt(16)
            val red = this.substring(2, 4).toInt(16)
            val green = this.substring(4, 6).toInt(16)
            val blue = this.substring(6, 8).toInt(16)
            Color(red, green, blue, alpha)
        }
        6 -> {
            // RRGGBB 형식일 경우
            val red = this.substring(0, 2).toInt(16)
            val green = this.substring(2, 4).toInt(16)
            val blue = this.substring(4, 6).toInt(16)
            Color(red, green, blue)
        }
        else -> {
            Color(0xFFFFFF)
        }
    }