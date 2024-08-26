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
        print("Command: ")
        val commandString = reader.nextLine()
        val command = parseCommand(commandString)
        displayResult(keyValueStore.applyCommand(command, 1))
    }
}

private fun parseCommand(command: String): KeyValueStoreContract.Command {
    val keyWords = command.split(" ")

    return when (keyWords.first()) {
        "SET" -> KeyValueStoreContract.Command.Set(keyWords[1], keyWords[2])
        "GET" -> KeyValueStoreContract.Command.Get(keyWords[1])
        "DELETE" -> KeyValueStoreContract.Command.Delete(keyWords[1])
        "COUNT" -> KeyValueStoreContract.Command.Count(keyWords[1])
        else -> {
            KeyValueStoreContract.Command.Get("")
        }
    }
}

private fun displayResult(result: KeyValueStoreContract.CommandResult) {
    when (result) {
        KeyValueStoreContract.CommandResult.Done -> println()
        is KeyValueStoreContract.CommandResult.Outcome -> println(result.value)
    }
}