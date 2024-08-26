package com.example.kv_store

class KeyValueStore {

    private val inMemoryStore = mutableMapOf<String, String>()

    private val transactionStack = TransactionStack()

    fun applyCommand(
        command: KeyValueStoreContract.Command,
    ): KeyValueStoreContract.CommandResult {

        return when (command) {
            is KeyValueStoreContract.Command.DataOperationCommand -> {
                return if (transactionStack.checkLock(command.clientId)) {
                    transactionStack.addCommand(command)
                } else {
                    command.apply(inMemoryStore)
                }
            }

            is KeyValueStoreContract.Command.TransactionOperationCommand -> {
                transactionStack.checkLock(command.clientId)
                transactionStack.handleTransactionOperation(
                    command
                )
                KeyValueStoreContract.CommandResult.Done
            }
        }
    }

    inner class TransactionStack {

        private val transactions = mutableListOf<PendingTransaction>()


        fun checkLock(clientId: Int): Boolean {
            if (transactions.isNotEmpty()) {
                if (clientId == transactions.last().ownerId) {
                    return true
                } else {
                    throw KeyValueStoreContract.AnotherTransactionInProgress()
                }
            }
            return false
        }

        fun handleTransactionOperation(command: KeyValueStoreContract.Command.TransactionOperationCommand) {
            when (command) {
                is KeyValueStoreContract.Command.TransactionOperationCommand.Begin -> startTransaction(
                    command.clientId
                )

                is KeyValueStoreContract.Command.TransactionOperationCommand.Commit -> commitTransaction()
                is KeyValueStoreContract.Command.TransactionOperationCommand.Rollback -> rollbackTransaction()
            }
        }

        private fun startTransaction(clientId: Int) {
            transactions.add(
                PendingTransaction(
                    clientId
                )
            )
        }

        private fun commitTransaction() {
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

        private fun rollbackTransaction() {
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

