package com.example.trustwallet.feature.data

import com.example.kv_store.KVException
import com.example.kv_store.KeyValueStore
import com.example.kv_store.KeyValueStoreContract
import com.example.trustwallet.feature.domain.Command
import com.example.trustwallet.feature.domain.usecase.ApplyCommandUseCase
import com.example.trustwallet.feature.domain.usecase.CommandUseCaseResult
import kotlin.random.Random

class StoreRepositoryImpl(
    private val keyValueStore: KeyValueStore
) : ApplyCommandUseCase {
    private val clientId: Int = Random.nextInt()

    override fun applyCommand(command: Command): CommandUseCaseResult {
        return try {
            val kvCommand = parseCommand(command)
            val result = keyValueStore.applyCommand(kvCommand)
            when (result) {
                is KeyValueStoreContract.CommandResult.Done -> CommandUseCaseResult.Success
                is KeyValueStoreContract.CommandResult.Outcome -> CommandUseCaseResult.Output(result.value)
            }
        } catch (e: KVException) {
            CommandUseCaseResult.Failure(e.message.orEmpty())
        } catch (e: Exception) {
            CommandUseCaseResult.Failure("unknown error")
        }
    }

    private fun parseCommand(
        command: Command
    ): KeyValueStoreContract.Command {
        return when (command) {
            is Command.Begin -> KeyValueStoreContract.Command.TransactionOperationCommand.Begin(
                clientId
            )

            is Command.Commit -> KeyValueStoreContract.Command.TransactionOperationCommand.Commit(
                clientId
            )

            is Command.Rollback -> KeyValueStoreContract.Command.TransactionOperationCommand.Rollback(
                clientId
            )

            is Command.Count -> KeyValueStoreContract.Command.DataOperationCommand.Count(
                command.key,
                clientId
            )

            is Command.Delete -> KeyValueStoreContract.Command.DataOperationCommand.Delete(
                command.key,
                clientId
            )

            is Command.Get -> KeyValueStoreContract.Command.DataOperationCommand.Get(
                command.key,
                clientId
            )

            is Command.Set -> KeyValueStoreContract.Command.DataOperationCommand.Set(
                command.key,
                command.value,
                clientId
            )
        }
    }
}