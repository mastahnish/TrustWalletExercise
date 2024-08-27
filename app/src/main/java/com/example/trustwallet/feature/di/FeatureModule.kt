package com.example.trustwallet.feature.di

import com.example.kv_store.KeyValueStore
import com.example.trustwallet.feature.data.StoreRepositoryImpl
import com.example.trustwallet.feature.domain.StoreRepository
import com.example.trustwallet.feature.domain.usecase.ApplyCommandUseCase
import com.example.trustwallet.feature.domain.usecase.ObserveCommandsHistoryUseCase
import com.example.trustwallet.feature.presentation.CommandsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val featureModule = module {
    viewModel {
        CommandsViewModel(
            get(),
            get(),
        )
    }

    factory {
        ObserveCommandsHistoryUseCase(
            get()
        )
    }

    single {
        StoreRepositoryImpl(get())
    }.bind(StoreRepository::class)
        .bind(ApplyCommandUseCase::class)

    single {
        KeyValueStore()
    }
}