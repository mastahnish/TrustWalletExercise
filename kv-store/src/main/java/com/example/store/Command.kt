package store

interface KeyValueStoreContract {
    sealed interface Command {

        sealed interface DataOperationCommand : Command {
            fun apply(map: MutableMap<String, String>): CommandResult

            data class Set(val key: String, val value: String) : DataOperationCommand {
                override fun apply(map: MutableMap<String, String>): CommandResult {
                    map[key] = value
                    return CommandResult.Outcome(
                        map[key] ?: throw KeyNotSetException()
                    )
                }
            }

            data class Get(val key: String) : DataOperationCommand {
                override fun apply(map: MutableMap<String, String>): CommandResult {
                    return CommandResult.Outcome(
                        map[key] ?: throw KeyNotSetException()
                    )
                }
            }

            data class Delete(val key: String) : DataOperationCommand {
                override fun apply(map: MutableMap<String, String>): CommandResult {
                    map.remove(key)
                    return CommandResult.Done
                }
            }

            data class Count(val value: String) : DataOperationCommand {
                override fun apply(map: MutableMap<String, String>): CommandResult {
                    val count = map.values.filter { it == value }.size
                    return CommandResult.Outcome(count.toString())
                }
            }
        }

        sealed interface TransactionOperationCommand : Command {
            data object Begin : TransactionOperationCommand

            data object Commit : TransactionOperationCommand

            data object Rollback : TransactionOperationCommand
        }

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

