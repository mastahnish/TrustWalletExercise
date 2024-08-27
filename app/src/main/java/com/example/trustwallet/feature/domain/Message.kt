package com.example.trustwallet.feature.domain

data class CommandMessage(
    val text: String,
    val isUser: Boolean,
    val error: String? = null
)
