package com.example.kv_store

interface KeyValueStoreContract {
    sealed class Command(open val clientId: Int) {

        sealed class DataOperationCommand(clientId: Int) : Command(clientId) {
            abstract fun apply(map: MutableMap<String, String>): CommandResult

            data class Set(val key: String, val value: String, override val clientId: Int) :
                DataOperationCommand(clientId) {
                override fun apply(map: MutableMap<String, String>): CommandResult {
                    map[key] = value
                    return CommandResult.Outcome(
                        map[key] ?: throw KeyNotSetException()
                    )
                }
            }

            data class Get(val key: String, override val clientId: Int) :
                DataOperationCommand(clientId) {
                override fun apply(map: MutableMap<String, String>): CommandResult {
                    return CommandResult.Outcome(
                        map[key] ?: throw KeyNotSetException()
                    )
                }
            }

            data class Delete(val key: String, override val clientId: Int) :
                DataOperationCommand(clientId) {
                override fun apply(map: MutableMap<String, String>): CommandResult {
                    map.remove(key)
                    return CommandResult.Done
                }
            }

            data class Count(val value: String, override val clientId: Int) :
                DataOperationCommand(clientId) {
                override fun apply(map: MutableMap<String, String>): CommandResult {
                    val count = map.values.filter { it == value }.size
                    return CommandResult.Outcome(count.toString())
                }
            }
        }

        sealed class TransactionOperationCommand(clientId: Int) : Command(clientId) {
            data class Begin(override val clientId: Int) : TransactionOperationCommand(clientId)

            data class Commit(override val clientId: Int) : TransactionOperationCommand(clientId)

            data class Rollback(override val clientId: Int) :
                TransactionOperationCommand(clientId)
        }

    }

    sealed interface CommandResult {
        data object Done : CommandResult

        data class Outcome(val value: String) : CommandResult
    }

    class KeyNotSetException : Exception("key is not set")

    class NoPendingTransaction : Exception("no transaction")

    class AnotherTransactionInProgress : Exception("another pending transaction")
}

