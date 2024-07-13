package com.hyosik.presentation.extension

fun Int?.orZero(): Int {
    return this ?: 0
}