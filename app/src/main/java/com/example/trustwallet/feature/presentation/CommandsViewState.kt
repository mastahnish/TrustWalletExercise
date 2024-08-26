package com.example.trustwallet.feature.presentation

data class CommandsViewState(
    val commands: List<Pair<String, Boolean>> = emptyList()
)