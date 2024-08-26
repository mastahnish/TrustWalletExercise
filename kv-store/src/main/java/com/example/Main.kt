import com.example.kv_store.KeyValueStore
import com.example.kv_store.KeyValueStoreContract
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
            displayResult(keyValueStore.applyCommand(command))
        } catch (e: Exception) {
            println(e.message)
            continue
        }
    }
}

private fun parseCommand(command: String): KeyValueStoreContract.Command {
    val keyWords = command.split(" ")
    val clientId = keyWords.getOrNull(3)?.toIntOrNull() ?: 1

    return when (keyWords.first()) {
        "SET" -> KeyValueStoreContract.Command.DataOperationCommand.Set(keyWords[1], keyWords[2],clientId)
        "GET" -> KeyValueStoreContract.Command.DataOperationCommand.Get(keyWords[1],clientId)
        "DELETE" -> KeyValueStoreContract.Command.DataOperationCommand.Delete(keyWords[1],clientId)
        "COUNT" -> KeyValueStoreContract.Command.DataOperationCommand.Count(keyWords[1],clientId)
        "ROLLBACK" -> KeyValueStoreContract.Command.TransactionOperationCommand.Rollback(clientId)
        "BEGIN" -> KeyValueStoreContract.Command.TransactionOperationCommand.Begin(clientId)
        "COMMIT" -> KeyValueStoreContract.Command.TransactionOperationCommand.Commit(clientId)
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