package com.example.trustwallet.feature.domain

sealed interface Command {
    data class Get(val key: String) : Command
    data class Set(val key: String, val value: String) : Command
    data class Delete(val key: String) : Command
    data class Count(val key: String) : Command
    data object Rollback : Command
    data object Commit : Command
    data object Begin : Command
}