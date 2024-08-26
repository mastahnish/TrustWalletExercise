package com.example.trustwallet.feature.presentation

import androidx.lifecycle.ViewModel
import com.example.trustwallet.feature.domain.usecase.ApplyCommandUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CommandsViewModel(
    private val applyCommandUseCase: ApplyCommandUseCase
) : ViewModel() {
    private val _state by lazy { MutableStateFlow(CommandsViewState()) }
    val state = _state.asStateFlow()
}