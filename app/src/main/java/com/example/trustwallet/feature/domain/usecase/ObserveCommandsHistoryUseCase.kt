package com.example.trustwallet.feature.domain.usecase

import com.example.trustwallet.feature.domain.StoreRepository

class ObserveCommandsHistoryUseCase(
    private val storeRepository: StoreRepository
) {
    operator fun invoke() = storeRepository.observeCommandsHistory()
}