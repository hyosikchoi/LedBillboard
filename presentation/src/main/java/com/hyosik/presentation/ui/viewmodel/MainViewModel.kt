package com.hyosik.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyosik.domain.usecase.GetBillboardUseCase
import com.hyosik.domain.usecase.PostBillboardUseCase
import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getBillboardUseCase: GetBillboardUseCase,
    private val postBillboardUseCase: PostBillboardUseCase,
) : ViewModel() {

    private var isFirst: Boolean = false

    private val _billboardState: MutableStateFlow<Billboard> = MutableStateFlow(
        Billboard(key = "", description = "")
    )

    val billboardState: StateFlow<Billboard> get() = _billboardState.asStateFlow()

    init {
        getSaveBillboard()
    }

    private fun getSaveBillboard() = viewModelScope.launch {
        getBillboardUseCase(BILLBOARD_KEY)
            .takeWhile { !isFirst }
            .collectLatest {
                  _billboardState.value = it
            }

    }

    fun saveBillboard(description: String) = viewModelScope.launch {
        postBillboardUseCase(Billboard(key = BILLBOARD_KEY, description = description))
    }

    fun getIsFirst() = isFirst

    fun setIsFirst() {
        isFirst = true
    }

}