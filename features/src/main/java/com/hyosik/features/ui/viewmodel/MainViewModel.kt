package com.hyosik.features.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyosik.core.ui.state.UiState
import com.hyosik.domain.usecase.GetBillboardUseCase
import com.hyosik.domain.usecase.PostBillboardUseCase
import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
import com.hyosik.features.ui.intent.MainEffect
import com.hyosik.features.ui.intent.MainEvent
import com.hyosik.features.ui.intent.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getBillboardUseCase: GetBillboardUseCase,
    private val postBillboardUseCase: PostBillboardUseCase,
) : ViewModel() {

    private val events = Channel<MainEvent>()
    //TODO presentation 모듈도 features 모듈로 변경하고 화면 별로 모듈을 나눈다.
    // theme 도 모듈로 나눈다.
    val state: StateFlow<UiState<MainState>> = events.receiveAsFlow()
        .onStart { getSaveBillboard() }
        .runningFold(
            initial = UiState<MainState>(
                data = MainState(
                    billboard = Billboard()
                )
            ),
            operation = ::reduceState
        )
        .stateIn(
            scope = viewModelScope,
            // 첫 번째 구독자가 나타나면 공유가 시작되고 마지막 구독자가 사라지면 즉시 중지됩니다.
            // 앱이 키 누름이나 화면 터치와 같은 입력 이벤트에 5초 이내에 응답하지 않으면 ANR이 발생합니다
            // 즉, 그 시점에서 UI를 더 이상 렌더링할 필요가 없거나 ANR이 이미 발생했을 수 있습니다.
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = UiState<MainState>(
                data =  MainState (
                billboard = Billboard()
            ))
        )

    private val _sideEffects: MutableSharedFlow<MainEffect> = MutableSharedFlow()

    val sideEffects: SharedFlow<MainEffect> = _sideEffects.asSharedFlow()

    private fun reduceState(current: UiState<MainState>, event: MainEvent): UiState<MainState> {
        return when (event) {
            is MainEvent.Initial -> {
                UiState.success(current.data?.copy(isInitialText = true, billboard = event.billboard) as MainState)
            }

            is MainEvent.Edit -> {
                UiState.success(current.data?.copy(billboard = event.billboard) as MainState)
            }

            is MainEvent.Save -> {
                viewModelScope.launch { postBillboardUseCase(billboard = event.billboard) }
                UiState.success(current.data?.copy(billboard = event.billboard) as MainState)
            }

            is MainEvent.SetTextWidth -> {
                UiState.success(current.data?.copy(billboard = current.data?.billboard?.copy(billboardTextWidth = event.textWidth) as Billboard) as MainState)
            }
        }
    }

    private fun getSaveBillboard() = viewModelScope.launch {
        getBillboardUseCase(BILLBOARD_KEY)
            // take 를 이용하여 처음 datastore 에 저장되어 있던 값만 수집하고 스트림을 종료시킨다.
            // (이렇게 안하면 TextField 에서 수정해서 저장할 때마다 해당 스트림을 통해 이벤트를 발생 시키기 때문이다!)
            .take(count = 1)
            .collect { billboard ->
                events.send(MainEvent.Initial(billboard = billboard))
            }
    }

    fun onEvent(event: MainEvent) = viewModelScope.launch {
        events.send(event)
    }

    fun saveBillboard(billboard: Billboard) = viewModelScope.launch {
        events.send(MainEvent.Edit(billboard = billboard))
        postBillboardUseCase(billboard = billboard)
    }

    fun setTextWidth(textWidth: Int) = viewModelScope.launch {
        events.send(MainEvent.Edit(state.value.data?.billboard?.copy(billboardTextWidth = textWidth) as Billboard))
    }

    fun sendSideEffect(effect: MainEffect) = viewModelScope.launch {
        _sideEffects.emit(effect)
    }

}