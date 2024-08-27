package com.example.trustwallet.feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trustwallet.feature.domain.Command
import com.example.trustwallet.feature.domain.usecase.ApplyCommandUseCase
import com.example.trustwallet.feature.domain.usecase.ObserveCommandsHistoryUseCase
import com.example.trustwallet.feature.domain.usecase.invoke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommandsViewModel(
    private val applyCommandUseCase: ApplyCommandUseCase,
    private val observeCommandsHistoryUseCase: ObserveCommandsHistoryUseCase
) : ViewModel() {
    private val _state by lazy { MutableStateFlow(CommandsViewState()) }
    val state = _state.asStateFlow()

    fun onStart() {
        viewModelScope.launch {
            observeCommandsHistoryUseCase.invoke().collect { commands ->
                _state.update {
                    it.copy(
                        commands = commands.toList(),
                    )
                }
            }
        }
    }

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
        _state.update {
            it.copy(showCommandMenu = true)
        }
    }

    fun onCommandMenuItemClicked(commandMenuItem: CommandMenuItem) {
        _state.update {
            it.copy(
                chosenCommand = commandMenuItem,
                showCommandMenu = false,
                parameter2 = "",
                parameter1 = "",
            )
        }
        onCommandMenuHide()
    }

    fun onCommandMenuHide() {
        _state.update {
            it.copy(showCommandMenu = false)
        }
    }

    fun onCommandSubmit() {
        viewModelScope.launch {
            applyCommandUseCase.invoke(
                with(state.value) {
                    when (chosenCommand) {
                        CommandMenuItem.SET -> Command.Set(
                            key = parameter1,
                            value = parameter2
                        )

                        CommandMenuItem.GET -> Command.Get(
                            key = parameter1
                        )

                        CommandMenuItem.DELETE -> Command.Delete(
                            key = parameter1
                        )

                        CommandMenuItem.COUNT -> Command.Count(
                            key = parameter1
                        )

                        CommandMenuItem.ROLLBACK -> Command.Rollback
                        CommandMenuItem.BEGIN -> Command.Begin
                        CommandMenuItem.COMMIT -> Command.Commit
                    }
                }
            )
        }
        clearParameters()
    }

    private fun clearParameters() {
        _state.update {
            it.copy(
                parameter1 = "",
                parameter2 = ""
            )
        }
    }
}