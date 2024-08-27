package com.example.trustwallet.feature.domain

sealed interface Command {
    data class Get(val key: String) : Command {
        override fun toString(): String = "GET $key"
    }

    data class Set(val key: String, val value: String) : Command {
        override fun toString(): String = "SET $key $value"
    }

    data class Delete(val key: String) : Command {
        override fun toString(): String = "DELETE $key"
    }

    data class Count(val key: String) : Command {
        override fun toString(): String = "COUNT $key"
    }

    data object Rollback : Command {
        override fun toString(): String = "ROLLBACK"
    }

    data object Commit : Command {
        override fun toString(): String = "COMMIT"
    }

    data object Begin : Command {
        override fun toString(): String = "BEGIN"
    }
}