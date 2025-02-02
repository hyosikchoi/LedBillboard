package com.hyosik.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import java.net.UnknownHostException

fun <T> Flow<T>.exception(tryRetry: Boolean, retryCount: Long): Flow<T> =
    retryWhen { cause, attempt ->
        if (tryRetry && attempt < retryCount) {
            // exception 관련 로직은 알아서 커스텀 가능
            when (cause) {
                is UnknownHostException -> false
                else -> true
            }
        } else {
            false
        }
    }