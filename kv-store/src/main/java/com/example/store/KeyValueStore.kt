package store

class KeyValueStore {

    private val inMemoryStore = mutableMapOf<String, String>()

    private val transactionStack = TransactionStack()

    fun applyCommand(command: KeyValueStoreContract.Command, sessionId: Int): KeyValueStoreContract.CommandResult =
        when (command) {
            is KeyValueStoreContract.Command.Get -> handleGet(command)
            is KeyValueStoreContract.Command.Set -> handleSet(command)
            is KeyValueStoreContract.Command.Delete -> handleDelete(command)
            is KeyValueStoreContract.Command.Count -> handleCount(command)
            KeyValueStoreContract.Command.Begin -> TODO()
            KeyValueStoreContract.Command.Commit -> TODO()
            KeyValueStoreContract.Command.Rollback -> TODO()
        }

    private fun handleSet(command: KeyValueStoreContract.Command.Set): KeyValueStoreContract.CommandResult {
        return if (transactionStack.isLocked()) {
            transactionStack.applyCommand(command)
        } else {
            inMemoryStore[command.key] = command.value
            KeyValueStoreContract.CommandResult.Outcome(
                inMemoryStore[command.key] ?: throw KeyValueStoreContract.KeyNotSetException()
            )
        }
    }

    private fun handleGet(command: KeyValueStoreContract.Command.Get): KeyValueStoreContract.CommandResult {
        return if (transactionStack.isLocked()) {
            transactionStack.applyCommand(command)
        } else {
            KeyValueStoreContract.CommandResult.Outcome(
                inMemoryStore[command.key] ?: throw KeyValueStoreContract.KeyNotSetException()
            )
        }
    }


    private fun handleDelete(command: KeyValueStoreContract.Command.Delete): KeyValueStoreContract.CommandResult {
        return if (transactionStack.isLocked()) {
            transactionStack.applyCommand(command)
        } else {
            inMemoryStore.remove(command.key)
            return KeyValueStoreContract.CommandResult.Done
        }
    }

    private fun handleCount(command: KeyValueStoreContract.Command.Count): KeyValueStoreContract.CommandResult {
        return if (transactionStack.isLocked()) {
            transactionStack.applyCommand(command)
        } else {
            val count = inMemoryStore.values.filter { it == command.value }.size
            return KeyValueStoreContract.CommandResult.Outcome(count.toString())
        }
    }

    private fun handleRollback(command: KeyValueStoreContract.Command.Rollback): KeyValueStoreContract.CommandResult {
        TODO()
    }

    private fun handleBegin(command: KeyValueStoreContract.Command.Begin): KeyValueStoreContract.CommandResult {
        TODO()
    }

    private fun handleCommit(command: KeyValueStoreContract.Command.Commit): KeyValueStoreContract.CommandResult {
        TODO()
    }

    private fun applyInternally(command: KeyValueStoreContract.Command, sessionId: Int) {
        applyCommand(command, sessionId)
    }

    inner class TransactionStack {

        private val transactions = mutableListOf<PendingTransaction>()

        fun isLocked(): Boolean = transactions.isNotEmpty()

        fun startTransaction() {
            transactions.add(
                PendingTransaction()
            )
        }

        fun commitTransaction() {
            try {
                transactions.last().commit()
            } catch (e: PendingTransaction.NoParentTransaction) {
                val transaction = transactions.removeLast()
                transaction.pendingCommands.forEach {
                    applyInternally(it, 0)
                }
            }
        }

        fun applyCommand(command: KeyValueStoreContract.Command): KeyValueStoreContract.CommandResult {
            transactions.last().pendingCommands.add(command)
            return calculateResult(command)
        }

        private fun calculateResult(command: KeyValueStoreContract.Command): KeyValueStoreContract.CommandResult.Done {
            val localCopy = inMemoryStore.toMap()
            return KeyValueStoreContract.CommandResult.Done
        }

        inner class PendingTransaction {
            val pendingCommands: MutableList<KeyValueStoreContract.Command> = mutableListOf()

            fun commit() {
                try {
                    val parentTransaction = transactions[transactions.size - 2]
                    parentTransaction.pendingCommands + transactions.removeLast()
                } catch (e: IndexOutOfBoundsException) {
                    throw NoParentTransaction()
                }
            }

            inner class NoParentTransaction : Exception()
        }
    }
}

