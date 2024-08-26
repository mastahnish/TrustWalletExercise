package com.example.trustwallet.feature.domain.usecase

sealed interface CommandUseCaseResult {
    data object Success : CommandUseCaseResult
    data class Output(val message: String) : CommandUseCaseResult
    data class Failure(val message: String) : CommandUseCaseResult
}