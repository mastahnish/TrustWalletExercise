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

enum class CommandMenuItem(val numOfParameters: Int) {
    SET(2),
    GET(1),
    DELETE(1),
    COUNT(1),
    BEGIN(0),
    COMMIT(0),
    ROLLBACK(0)
}