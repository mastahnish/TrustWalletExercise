package store

interface KeyValueStoreContract {
    sealed interface Command {
        data class Set(val key: String, val value: String) : Command

        data class Get(val key: String) : Command

        data class Delete(val key: String) : Command

        data class Count(val value: String) : Command

        data object Begin : Command

        data object Commit : Command

        data object Rollback : Command
    }

    sealed interface CommandResult {
        data object Done : CommandResult

        data class Outcome(val value: String) : CommandResult
    }

    class KeyNotSetException : Exception("key is not set")

    class NoPendingTransaction : Exception("there is not pending transaction")

    class AnotherTransactionInProgress : Exception("There is another transaction")

    class UnknownException(val notHandledException: Exception) : Exception()
}

