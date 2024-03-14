package org.example.commands.menu

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.entities.MenuItem
import org.example.utils.ConsoleInputHandler
import org.example.utils.OutputConsole
import org.example.utils.OutputMessageType

class RemoveMenuItemAppCommand(override var description: String = "Удалить блюдо") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        if (!argument.menuStorage.getMenuItems().any()) {
            println("Меню пусто")
            println()
            ConsoleInputHandler.readEnterPress()
            return
        }

        var itemToRemove: MenuItem? = null

        do {
            val id = ConsoleInputHandler.readIntCheckBackCommand("Введите id блюда, которое хотите удалить: ", argument.backCommand)
                ?: return

            itemToRemove = argument.menuStorage.getMenuItem(id)
            if (itemToRemove == null) {
                OutputConsole.printMessage("Блюда с таким id нет в меню!", OutputMessageType.Error)
            }
        } while (itemToRemove == null)

        if (argument.menuStorage.removeMenuItem(itemToRemove.id)) {
            argument.statisticsManager.menuItemRemoved(itemToRemove)
            OutputConsole.printMessage("Блюдо удалено из меню", OutputMessageType.Success)
            OutputConsole.printMessage("Все оформленные заказы с данным блюдом останутся неизменными")
            ConsoleInputHandler.readEnterPress()
        }
    }
}