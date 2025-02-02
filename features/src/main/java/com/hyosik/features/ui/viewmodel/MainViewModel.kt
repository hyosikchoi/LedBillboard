package com.hyosik.features.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.hyosik.common.BaseViewModel
import com.hyosik.core.ui.state.UiState
import com.hyosik.domain.usecase.GetBillboardUseCase
import com.hyosik.domain.usecase.PostBillboardUseCase
import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
import com.hyosik.features.ui.intent.MainEffect
import com.hyosik.features.ui.intent.MainEvent
import com.hyosik.features.ui.intent.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
) : BaseViewModel<MainEvent, MainState, MainEffect>() {

    override fun setInitialState(): UiState<MainState> =
        UiState(
            data = MainState(
                billboard = Billboard()
            )
        )

    override fun reduceState(
        currentState: UiState<MainState>,
        event: MainEvent
    ): UiState<MainState> {
        return when (event) {
            is MainEvent.Initial -> {
                UiState.success(currentState.data?.copy(isInitialText = true, billboard = event.billboard) as MainState)
            }

            is MainEvent.Edit -> {
                UiState.success(currentState.data?.copy(billboard = event.billboard) as MainState)
            }

            is MainEvent.Save -> {
                viewModelScope.launch { postBillboardUseCase(billboard = event.billboard) }
                UiState.success(currentState.data?.copy(billboard = event.billboard) as MainState)
            }

            is MainEvent.SetTextWidth -> {
                UiState.success(currentState.data?.copy(billboard = currentState.data?.billboard?.copy(billboardTextWidth = event.textWidth) as Billboard) as MainState)
            }

        }
    }

    //TODO presentation 모듈도 features 모듈로 변경하고 화면 별로 모듈을 나눈다.
    // theme 도 모듈로 나눈다.
    // init 에서 호출 하게 되면 단위 테스트 시 이 init 함수는 계속 걸고 넘어지게 된다.
    override val state: StateFlow<UiState<MainState>> = events.receiveAsFlow()
        .onStart {
            getBillboardUseCase(BILLBOARD_KEY)
                // take 를 이용하여 처음 datastore 에 저장되어 있던 값만 수집하고 스트림을 종료시킨다.
                // (이렇게 안하면 TextField 에서 수정해서 저장할 때마다 해당 스트림을 통해 이벤트를 발생 시키기 때문이다!)
                .take(count = 1)
                .collect { billboard ->
                    setEvent(MainEvent.Initial(billboard = billboard))
                }
        }
        .runningFold(
            initial = initialUiState,
            operation = ::reduceState
        ).stateIn(viewModelScope, SharingStarted.Eagerly, initialUiState)


    fun saveBillboard(billboard: Billboard) = viewModelScope.launch {
        setEvent(MainEvent.Edit(billboard = billboard))
        postBillboardUseCase(billboard = billboard)
    }

//    fun setTextWidth(textWidth: Int) = viewModelScope.launch {
//        events.send(MainEvent.Edit(state.value.data?.billboard?.copy(billboardTextWidth = textWidth) as Billboard))
//    }



}