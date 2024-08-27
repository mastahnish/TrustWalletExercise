package com.example.trustwallet.feature.presentation

import com.example.trustwallet.feature.domain.CommandMessage

data class CommandsViewState(
    val commands: List<CommandMessage> = emptyList(),
    val chosenCommand: CommandMenuItem = CommandMenuItem.GET,
    val availableCommandItems: List<CommandMenuItem> = CommandMenuItem.entries,
    val parameter1: String = "",
    val parameter2: String = "",
    val showCommandMenu: Boolean = false
){
    fun isParameter1Visible() = chosenCommand.numOfParameters >= 1

    fun isParameter2Visible() = chosenCommand.numOfParameters >= 2
}

enum class CommandMenuItem(val numOfParameters: Int) {
    SET(2),
    GET(1),
    DELETE(1),
    COUNT(1),
    BEGIN(0),
    COMMIT(0),
    ROLLBACK(0)
}