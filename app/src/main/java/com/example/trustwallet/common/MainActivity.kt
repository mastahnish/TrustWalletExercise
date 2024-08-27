package com.example.trustwallet.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.trustwallet.common.theme.TrustWalletTheme
import com.example.trustwallet.feature.presentation.CommandsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrustWalletTheme {
                CommandsScreen()
            }
        }
    }
}