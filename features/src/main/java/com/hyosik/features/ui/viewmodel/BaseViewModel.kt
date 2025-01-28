package com.hyosik.features.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyosik.core.ui.state.UiState
import com.hyosik.features.ui.intent.Effect
import com.hyosik.features.ui.intent.Event
import com.hyosik.features.ui.intent.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


abstract class BaseViewModel<E: Event, S: State, T: Effect>: ViewModel() {

    abstract fun setInitialState(): UiState<S>

    abstract fun reduceState(currentState: UiState<S>, event: E): UiState<S>

    private val initialUiState: UiState<S> by lazy { setInitialState() }

    private val events = Channel<E>()

    private val _effects = Channel<T>()

    val state: StateFlow<UiState<S>> = events.receiveAsFlow()
        .runningFold(
            initial = initialUiState,
            operation = ::reduceState
        ).stateIn(viewModelScope, SharingStarted.Eagerly, initialUiState)

    val effects get() = _effects.receiveAsFlow()

    fun setEvent(event: E) = viewModelScope.launch {
        events.send(event)
    }

    fun setEffect(effect: T) = viewModelScope.launch {
        _effects.send(effect)
    }

}