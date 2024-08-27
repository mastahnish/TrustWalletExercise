package com.example.trustwallet.feature.data

import com.example.kv_store.KVException
import com.example.kv_store.KeyValueStore
import com.example.kv_store.KeyValueStoreContract
import com.example.trustwallet.feature.domain.Command
import com.example.trustwallet.feature.domain.CommandMessage
import com.example.trustwallet.feature.domain.StoreRepository
import com.example.trustwallet.feature.domain.usecase.ApplyCommandUseCase
import com.example.trustwallet.feature.domain.usecase.CommandUseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.random.Random

class StoreRepositoryImpl(
    private val keyValueStore: KeyValueStore
) : ApplyCommandUseCase, StoreRepository {
    private val clientId: Int = Random.nextInt()

    private val commandsHistory: MutableList<CommandMessage> = mutableListOf()

    private val commandsHistoryFlow = MutableSharedFlow<List<CommandMessage>>(
        replay = 1,
        extraBufferCapacity = 1,
    )

    override fun observeCommandsHistory(): Flow<List<CommandMessage>> {
        commandsHistoryFlow.tryEmit(commandsHistory)
        return commandsHistoryFlow
    }

    override fun applyCommand(command: Command) {
        try {
            val kvCommand = parseCommand(command)
            doAndNotify {
                commandsHistory.add(
                    CommandMessage(
                        text = command.toString(),
                        isUser = true
                    )
                )
            }
            when (val result = keyValueStore.applyCommand(kvCommand)) {
                is KeyValueStoreContract.CommandResult.Done -> CommandUseCaseResult.Success
                is KeyValueStoreContract.CommandResult.Outcome -> doAndNotify {
                    commandsHistory.add(
                        CommandMessage(
                            text = result.value,
                            isUser = false,
                        )
                    )
                }
            }
        } catch (e: KVException) {
            handleFailure(e.message.orEmpty())
        } catch (e: Exception) {
            handleFailure("unknown error")
        }
    }

    private fun doAndNotify(block: (List<CommandMessage>) -> Unit) {
        block(commandsHistory)
        commandsHistoryFlow.tryEmit(commandsHistory)
    }

    private fun handleFailure(error: String) = doAndNotify {
        val lastMessage = commandsHistory.removeLastOrNull()
        lastMessage?.let {
            lastMessage.copy(error = error).also {
                commandsHistory.add(it)
            }
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