package store

import com.example.store.KeyValueStoreContract

class KeyValueStore {

    private val inMemoryStore = mutableMapOf<String, String>()

    private val transactionStack = TransactionStack()

    fun applyCommand(
        command: KeyValueStoreContract.Command,
        clientId: Int
    ): KeyValueStoreContract.CommandResult {

        return when (command) {
            is KeyValueStoreContract.Command.DataOperationCommand.Get -> handleGet(command)
            is KeyValueStoreContract.Command.DataOperationCommand.Set -> handleSet(command)
            is KeyValueStoreContract.Command.DataOperationCommand.Delete -> handleDelete(command)
            is KeyValueStoreContract.Command.DataOperationCommand.Count -> handleCount(command)
            is KeyValueStoreContract.Command.TransactionOperationCommand.Begin -> handleBegin(
                clientId
            )

            is KeyValueStoreContract.Command.TransactionOperationCommand.Commit -> handleCommit()
            is KeyValueStoreContract.Command.TransactionOperationCommand.Rollback -> handleRollback()
        }
    }


    private fun handleSet(command: KeyValueStoreContract.Command.DataOperationCommand.Set): KeyValueStoreContract.CommandResult {
        return if (transactionStack.isLocked(command.clientId)) {
            transactionStack.addCommand(command)
        } else {
            command.apply(inMemoryStore)
        }
    }

    private fun handleGet(command: KeyValueStoreContract.Command.DataOperationCommand.Get): KeyValueStoreContract.CommandResult {
        return if (transactionStack.isLocked(command.clientId)) {
            transactionStack.addCommand(command)
        } else {
            command.apply(inMemoryStore)
        }
    }


    private fun handleDelete(command: KeyValueStoreContract.Command.DataOperationCommand.Delete): KeyValueStoreContract.CommandResult {
        return if (transactionStack.isLocked(command.clientId)) {
            transactionStack.addCommand(command)
        } else {
            command.apply(inMemoryStore)
        }
    }

    private fun handleCount(command: KeyValueStoreContract.Command.DataOperationCommand.Count): KeyValueStoreContract.CommandResult {
        return if (transactionStack.isLocked(command.clientId)) {
            transactionStack.addCommand(command)
        } else {
            command.apply(inMemoryStore)
        }
    }

    private fun handleRollback(): KeyValueStoreContract.CommandResult =
        transactionStack.rollbackTransaction().let { KeyValueStoreContract.CommandResult.Done }

    private fun handleBegin(clientId: Int): KeyValueStoreContract.CommandResult =
        transactionStack.startTransaction(clientId).let { KeyValueStoreContract.CommandResult.Done }

    private fun handleCommit(): KeyValueStoreContract.CommandResult =
        transactionStack.commitTransaction().let { KeyValueStoreContract.CommandResult.Done }

    inner class TransactionStack {

        private val transactions = mutableListOf<PendingTransaction>()

        fun isLocked(clientId: Int): Boolean {
            if (transactions.isNotEmpty()) {
                if (clientId == transactions.last().ownerId) {
                    return true
                } else {
                    throw KeyValueStoreContract.AnotherTransactionInProgress()
                }
            }
            return false
        }

        fun startTransaction(clientId: Int) {
            transactions.add(
                PendingTransaction(
                    clientId
                )
            )
        }

        fun commitTransaction() {
            try {
                transactions.last().commit()
            } catch (e: PendingTransaction.NoParentTransaction) {
                val transaction = transactions.removeLast()
                transaction.pendingCommands.forEach {
                    it.apply(inMemoryStore)
                }
            } catch (e: NoSuchElementException) {
                throw KeyValueStoreContract.NoPendingTransaction()
            }
        }

        fun rollbackTransaction() {
            try {
                transactions.removeLast()
            } catch (e: NoSuchElementException) {
                throw KeyValueStoreContract.NoPendingTransaction()
            }
        }

        fun addCommand(command: KeyValueStoreContract.Command.DataOperationCommand): KeyValueStoreContract.CommandResult {
            val simulatedResult = runLocally(command)
            transactions.last().pendingCommands.add(command)
            return simulatedResult
        }

        private fun runLocally(command: KeyValueStoreContract.Command.DataOperationCommand): KeyValueStoreContract.CommandResult {
            val localCopy = inMemoryStore.toMutableMap()
            transactions.forEach { transaction ->
                transaction.pendingCommands.forEach { transactionCommand ->
                    transactionCommand.apply(localCopy)
                }
            }
            return command.apply(localCopy)
        }

        inner class PendingTransaction(
            val ownerId: Int
        ) {
            val pendingCommands: MutableList<KeyValueStoreContract.Command.DataOperationCommand> =
                mutableListOf()

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

