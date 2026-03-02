package com.hyosik.features.extension

fun Int?.orZero(): Int {
    return this ?: 0
}