package com.example.trustwallet.feature.presentation

import androidx.lifecycle.ViewModel
import com.example.trustwallet.feature.domain.usecase.ApplyCommandUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CommandsViewModel(
    private val applyCommandUseCase: ApplyCommandUseCase
) : ViewModel() {
    private val _state by lazy { MutableStateFlow(CommandsViewState()) }
    val state = _state.asStateFlow()

    fun onParameter1Change(text: String) {
        _state.update {
            it.copy(parameter1 = text)
        }
    }

    fun onParameter2Change(text: String) {
        _state.update {
            it.copy(parameter2 = text)
        }
    }

    fun onCommandMenuClicked() {

    }

    fun onCommandSubmit() {

    }
}