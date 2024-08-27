package com.example.kv_store

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class KeyValueStoreTest {

    private lateinit var keyValueStore: KeyValueStore

    @Before
    fun setUp() {
        keyValueStore = KeyValueStore()
    }

    @Test
    fun `GIVEN new value WHEN SET command THEN result`() {
        val result = keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Set(
                clientId = 1,
                key = "key",
                value = "value"
            )
        )
        assertEquals(result, KeyValueStoreContract.CommandResult.Outcome("value"))
    }

    @Test
    fun `GIVEN value WHEN GET command THEN result`() {
        keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Set(
                clientId = 1,
                key = "key",
                value = "value"
            )
        )
        val result = keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Get(
                clientId = 1,
                key = "key"
            )
        )
        assertEquals(result, KeyValueStoreContract.CommandResult.Outcome("value"))
    }

    @Test
    fun `GIVEN no value WHEN GET command THEN exception thrown`() {
        assertThrows(KVException.KeyNotSetException::class.java) {
            keyValueStore.applyCommand(
                KeyValueStoreContract.Command.DataOperationCommand.Get(
                    clientId = 1,
                    key = "key"
                )
            )
        }
    }

    @Test
    fun `GIVEN value WHEN COUNT command THEN result`() {
        keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Set(
                clientId = 1,
                key = "key",
                value = "value"
            )
        )
        val result = keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Count(
                clientId = 1,
                value = "value"
            )
        )
        assertEquals(result, KeyValueStoreContract.CommandResult.Outcome("1"))
    }

    @Test
    fun `GIVEN value WHEN DELETE command THEN result`() {
        keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Set(
                clientId = 1,
                key = "key",
                value = "value"
            )
        )
        val result = keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Delete(
                clientId = 1,
                key = "key"
            )
        )
        assertEquals(result, KeyValueStoreContract.CommandResult.Done)
    }

    @Test
    fun `GIVEN no value WHEN DELETE command THEN result`() {
        val result = keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Delete(
                clientId = 1,
                key = "key"
            )
        )
        assertEquals(result, KeyValueStoreContract.CommandResult.Done)
    }

    @Test
    fun `GIVEN no value WHEN COUNT command THEN result`() {
        val result = keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Count(
                clientId = 1,
                value = "value"
            )
        )
        assertEquals(result, KeyValueStoreContract.CommandResult.Outcome("0"))
    }

    @Test
    fun `GIVEN no transaction WHEN ROLLBACK command THEN exception thrown`() {
        assertThrows(KVException.NoPendingTransaction::class.java) {
            keyValueStore.applyCommand(
                KeyValueStoreContract.Command.TransactionOperationCommand.Rollback(
                    clientId = 1
                )
            )
        }
    }

    @Test
    fun `GIVEN no transaction WHEN COMMIT command THEN exception thrown`() {
        assertThrows(KVException.NoPendingTransaction::class.java) {
            keyValueStore.applyCommand(
                KeyValueStoreContract.Command.TransactionOperationCommand.Commit(
                    clientId = 1
                )
            )
        }
    }

    @Test
    fun `GIVEN no transaction WHEN BEGIN command THEN result`() {
        val result = keyValueStore.applyCommand(
            KeyValueStoreContract.Command.TransactionOperationCommand.Begin(
                clientId = 1
            )
        )
        assertEquals(result, KeyValueStoreContract.CommandResult.Done)
    }

    @Test
    fun `GIVEN transaction WHEN commit THEN data available`() {
        keyValueStore.applyCommand(
            KeyValueStoreContract.Command.TransactionOperationCommand.Begin(
                clientId = 1
            )
        )
        keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Set(
                clientId = 1,
                key = "key",
                value = "value"
            )
        )
        keyValueStore.applyCommand(
            KeyValueStoreContract.Command.TransactionOperationCommand.Commit(
                clientId = 1
            )
        )
        val result = keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Get(
                clientId = 1,
                key = "key"
            )
        )
        assertEquals(result, KeyValueStoreContract.CommandResult.Outcome("value"))
    }

    @Test
    fun `GIVEN transaction WHEN rollback THEN data not available`() {
        keyValueStore.applyCommand(
            KeyValueStoreContract.Command.TransactionOperationCommand.Begin(
                clientId = 1
            )
        )
        keyValueStore.applyCommand(
            KeyValueStoreContract.Command.DataOperationCommand.Set(
                clientId = 1,
                key = "key",
                value = "value"
            )
        )
        keyValueStore.applyCommand(
            KeyValueStoreContract.Command.TransactionOperationCommand.Rollback(
                clientId = 1
            )
        )
        assertThrows(KVException.KeyNotSetException::class.java) {
            keyValueStore.applyCommand(
                KeyValueStoreContract.Command.DataOperationCommand.Get(
                    clientId = 1,
                    key = "key"
                )
            )
        }
    }
}