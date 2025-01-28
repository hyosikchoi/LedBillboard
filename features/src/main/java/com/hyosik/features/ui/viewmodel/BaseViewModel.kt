package com.hyosik.features.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyosik.core.ui.state.UiState
import com.hyosik.features.ui.intent.Effect
import com.hyosik.features.ui.intent.Event
import com.hyosik.features.ui.intent.State
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


abstract class BaseViewModel<E: Event, S: State, T: Effect>: ViewModel() {

    abstract fun setInitialState(): UiState<S>

    abstract fun reduceState(currentState: UiState<S>, event: E): UiState<S>

    abstract val state: StateFlow<UiState<S>>

    private val _events = Channel<E>()

    private val _effects = Channel<T>()

    val events get() = _events

    val effects get() = _effects.receiveAsFlow()

    val initialUiState: UiState<S> by lazy { setInitialState() }

    fun setEvent(event: E) = viewModelScope.launch {
        _events.send(event)
    }

    fun setEffect(effect: T) = viewModelScope.launch {
        _effects.send(effect)
    }

}