package com.example.demo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class UiType {
    HOME,
    SMS;
}
class UiViewModel( private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default): ViewModel() {
    private val _uiState = MutableStateFlow(UiType.HOME)
    val uiState: StateFlow<UiType> = _uiState.asStateFlow()
    fun update(type: UiType){
        _uiState.value = type
    }
}