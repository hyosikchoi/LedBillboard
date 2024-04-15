package com.hyosik.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyosik.domain.usecase.GetBillboardUseCase
import com.hyosik.domain.usecase.PostBillboardUseCase
import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getBillboardUseCase: GetBillboardUseCase,
    private val postBillboardUseCase: PostBillboardUseCase,
) : ViewModel() {

    private var isFirst: Boolean = true

    private val _billboardState: MutableSharedFlow<Billboard> = MutableSharedFlow(replay = 0)

    val billboardState: SharedFlow<Billboard> get() = _billboardState.asSharedFlow()

    init {
        getSaveBillboard()
    }

    fun getSaveBillboard() = viewModelScope.launch {
        getBillboardUseCase(BILLBOARD_KEY)
            .collect { billboard: Billboard ->
                _billboardState.emit(value = billboard)
            }
    }

    fun saveBillboard(description: String) = viewModelScope.launch {
        postBillboardUseCase(Billboard(key = BILLBOARD_KEY, description = description))
    }

    fun getIsFirstGetBillboard(): Boolean = isFirst

    fun setIsFirstGetBillboard() {
        isFirst = false
    }

}