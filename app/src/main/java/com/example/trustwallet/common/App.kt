package com.example.trustwallet.common

import android.app.Application
import com.example.trustwallet.feature.di.featureModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(featureModule)
        }
    }
}