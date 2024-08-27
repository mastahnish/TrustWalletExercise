package com.example.trustwallet.feature.domain.usecase

import com.example.trustwallet.feature.domain.Command

interface ApplyCommandUseCase {
    fun applyCommand(command: Command): CommandUseCaseResult
}

fun ApplyCommandUseCase.invoke(command: Command) {
    applyCommand(command)
}