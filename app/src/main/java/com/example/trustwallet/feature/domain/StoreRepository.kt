package com.example.trustwallet.feature.domain

import com.example.trustwallet.feature.domain.usecase.ApplyCommandUseCase
import kotlinx.coroutines.flow.Flow

interface StoreRepository : ApplyCommandUseCase {
    fun observeCommandsHistory(): Flow<List<CommandMessage>>
}