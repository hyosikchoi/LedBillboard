package com.hyosik.core.ui.state

data class UiState<T>(
    val isLoading: Boolean = false,
    val cause: Throwable? = null,
    val data: T? = null
) {
    val isError: Boolean = cause != null

    val isSuccess: Boolean = data != null

    companion object {
        fun <T> loading() = UiState<T>(isLoading = true)

        fun <T> success(data: T) = UiState(data = data)

        fun <T> error(cause: Throwable) = UiState<T>(cause = cause)
    }
}
