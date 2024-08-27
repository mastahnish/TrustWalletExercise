package com.example.trustwallet.feature.presentation

data class CommandsViewState(
    val commands: List<Pair<String, Boolean>> = emptyList(),
    val chosenCommand: CommandMenuItem = CommandMenuItem.GET,
    val availableCommandItems: List<CommandMenuItem> = CommandMenuItem.entries,
    val parameter1: String = "",
    val isParameter1Visible: Boolean = true,
    val parameter2: String = "",
    val isParameter2Visible: Boolean = true,
    val showCommandMenu: Boolean = false
)

enum class CommandMenuItem {
    SET, GET, DELETE, COUNT, ROLLBACK, BEGIN, COMMIT
}