package com.example.trustwallet.feature.di

import com.example.kv_store.KeyValueStore
import com.example.trustwallet.feature.data.StoreRepositoryImpl
import com.example.trustwallet.feature.domain.usecase.ApplyCommandUseCase
import com.example.trustwallet.feature.presentation.CommandsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureModule = module {
    viewModel {
        CommandsViewModel(get())
    }

    single<ApplyCommandUseCase> {
        StoreRepositoryImpl(get())
    }

    single {
        KeyValueStore()
    }
}