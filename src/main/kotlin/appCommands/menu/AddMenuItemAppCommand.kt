package org.example.commands.menu

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.utils.ConsoleInputHandler
import org.example.utils.OutputConsole
import org.example.utils.OutputMessageType

class AddMenuItemAppCommand(override var description: String = "Создать блюдо") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        val name: String = readName(argument) ?: return
        val price: Int = readPositiveInt("Введите цену за одну порцию: ", argument.backCommand) ?: return
        val quantity: Int = readPositiveInt("Введите количество порций в наличии: ", argument.backCommand) ?: return
        val timeToCook: Int = readPositiveInt("Введите время приготовления одной порции в минутах: ", argument.backCommand) ?: return

        val item = argument.menuStorage.createMenuItem(name, quantity, price, timeToCook)
        argument.statisticsManager.menuItemAdded(item)
        OutputConsole.printMessage("Блюдо добавлено в меню", OutputMessageType.Success)
        ConsoleInputHandler.readEnterPress()
    }

    private fun readName(application: ApplicationCore): String? {
        var name: String? = null
        do {
            name = ConsoleInputHandler.readNotEmptyString("Введите название блюда: ")
            if (name == application.backCommand) {
                return null
            }
            if (application.menuStorage.getMenuItems().any{it.name == name}) {
                OutputConsole.printMessage("Блюдо с таким названием уже есть в меню!", OutputMessageType.Error)
                name = null
            }
        } while(name == null)

        return name
    }
    private fun readPositiveInt(message: String, backCommand: String): Int? {
        var num: Int? = null
        do {
            num = ConsoleInputHandler.readIntCheckBackCommand(message, backCommand)
            if (num == null) {
                return null
            }

            if (num <= 0) {
                OutputConsole.printMessage("Число должно быть положительным!", OutputMessageType.Error)
                num = null
            }

        } while (num == null)

        return num
    }
}