package org.example.utils

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.commandFactories.AppCommandFactory

class CommandReader<T>(userCommandSetFactory: AppCommandFactory<T>) {
    private val _commands: MutableMap<Int, AppCommand> = mutableMapOf()

    init {
        var commandNumber = 1
        userCommandSetFactory.createCommandSet().forEach{_commands[commandNumber++] = it}
    }

    fun displayCommands() {
        if (_commands.isEmpty()) {
            println("Нет доступных команд.")
            return
        }

        _commands.forEach{ println("${it.key}. ${it.value.description}") }
    }

    fun readAndExecute(argument: ApplicationCore) {
        if (_commands.isEmpty()) {
            return
        }

        var toExecute: AppCommand? = null

        do {
            toExecute = _commands[ConsoleInputHandler.readInt("Введите номер команды: ")]

            if (toExecute == null) {
                OutputConsole.printMessage("Команды с таким номером нет в списке!", OutputMessageType.Error)
            }
        } while (toExecute == null)

        toExecute.execute(argument)
    }
}