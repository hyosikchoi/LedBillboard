package com.hyosik.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyosik.domain.usecase.GetBillboardUseCase
import com.hyosik.domain.usecase.PostBillboardUseCase
import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
import com.hyosik.model.Direction
import com.hyosik.presentation.ui.intent.MainEvent
import com.hyosik.presentation.ui.intent.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getBillboardUseCase: GetBillboardUseCase,
    private val postBillboardUseCase: PostBillboardUseCase,
) : ViewModel() {

    private val events = Channel<MainEvent>()

    val state: StateFlow<MainState> = events.receiveAsFlow()
        .runningFold(
            initial = MainState(
                billboard = Billboard()
            ),
            operation = ::reduceState
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = MainState(
                billboard = Billboard()
            )
        )

    init {
        getSaveBillboard()
    }

    private fun reduceState(current: MainState, event: MainEvent): MainState {
        return when (event) {
            is MainEvent.Initial -> {
                current.copy(isInitialText = true, billboard = event.billboard)
            }

            is MainEvent.Edit -> {
                current.copy(billboard = event.billboard)
            }
        }
    }

    private fun getSaveBillboard() = viewModelScope.launch {
        getBillboardUseCase(BILLBOARD_KEY)
//            .takeWhile { state.value.isInitialText }
            .collectLatest {
               if(state.value.isInitialText.not()) events.send(MainEvent.Initial(billboard = it))
            }
    }

    fun saveBillboard(billboard: Billboard) = viewModelScope.launch {
        events.send(MainEvent.Edit(billboard = billboard))
        postBillboardUseCase(billboard = billboard)
    }

    fun setTextWidth(textWidth: Int) = viewModelScope.launch {
        events.send(MainEvent.Edit(state.value.billboard.copy(billboardTextWidth = textWidth)))
    }

}