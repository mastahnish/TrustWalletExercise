import store.KeyValueStore
import store.KeyValueStoreContract
import java.util.Scanner

/**
 * This file is only for key value store implementation test purpose
 */
fun main() {
    val keyValueStore = KeyValueStore()
    val reader = Scanner(System.`in`)
    while (true) {
        try {
            print("Command: ")
            val commandString = reader.nextLine()
            val command = parseCommand(commandString)
            displayResult(keyValueStore.applyCommand(command, 1))
        } catch (e: Exception) {
            println(e.message)
            continue
        }
    }
}

private fun parseCommand(command: String): KeyValueStoreContract.Command {
    val keyWords = command.split(" ")

    return when (keyWords.first()) {
        "SET" -> KeyValueStoreContract.Command.DataOperationCommand.Set(keyWords[1], keyWords[2], 1)
        "GET" -> KeyValueStoreContract.Command.DataOperationCommand.Get(keyWords[1], 1)
        "DELETE" -> KeyValueStoreContract.Command.DataOperationCommand.Delete(keyWords[1], 1)
        "COUNT" -> KeyValueStoreContract.Command.DataOperationCommand.Count(keyWords[1], 1)
        "ROLLBACK" -> KeyValueStoreContract.Command.TransactionOperationCommand.Rollback(1)
        "BEGIN" -> KeyValueStoreContract.Command.TransactionOperationCommand.Begin(1)
        "COMMIT" -> KeyValueStoreContract.Command.TransactionOperationCommand.Commit(1)
        else -> {
            throw Exception("WTF is that command?")
        }
    }
}

private fun displayResult(result: KeyValueStoreContract.CommandResult) {
    when (result) {
        KeyValueStoreContract.CommandResult.Done -> println()
        is KeyValueStoreContract.CommandResult.Outcome -> println(result.value)
    }
}