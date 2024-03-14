package org.example.commands.menu

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.crud.MenuItemStorage
import org.example.entities.MenuItem
import org.example.utils.ConsoleInputHandler
import org.example.utils.OutputConsole
import org.example.utils.OutputMessageType

class EditMenuItemAppCommand(override var description: String = "Редактировать блюдо") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        var menuItemToEdit: MenuItem? = null

        do {
            val id = ConsoleInputHandler.readIntCheckBackCommand("Введите id блюда: ", argument.backCommand) ?: return
            menuItemToEdit = argument.menuStorage.getMenuItem(id)

            if (menuItemToEdit == null) {
                OutputConsole.printMessage("Блюда с таким id нет!", OutputMessageType.Error)
            }
        } while (menuItemToEdit == null)

        OutputConsole.printMessage("Модификация блюда ${menuItemToEdit.name}:")

        val newName = readNewName(
            "Введите новое название блюда. Чтобы оставить прежнее значение (${menuItemToEdit.name}), введите '-': ",
            menuItemToEdit.name,
            argument.backCommand,
            argument.menuStorage)
        menuItemToEdit.name = newName ?: return

        val newPrice = readNewPositiveInt(
            "Введите новую цену за порцию. Чтобы оставить прежнее значение (${menuItemToEdit.price}), введите '-': ",
            menuItemToEdit.price,
            argument.backCommand)
        menuItemToEdit.price = newPrice ?: return

        val newQuantity = readNewPositiveInt(
            "Введите новое количество порций. Чтобы оставить прежнее значение (${menuItemToEdit.quantity}), введите '-': ",
            menuItemToEdit.quantity,
            argument.backCommand)
        menuItemToEdit.quantity = newQuantity ?: return

        val newTime = readNewPositiveInt(
            "Введите новое время готовки порции. Чтобы оставить прежнее значение (${menuItemToEdit.timeToCook}), введите '-': ",
            menuItemToEdit.timeToCook,
            argument.backCommand)
        menuItemToEdit.timeToCook = newTime ?: return

        OutputConsole.printMessage("Данные изменены", OutputMessageType.Success)
        OutputConsole.printMessage("Все заказы, содержащие прежние данные этого блюда, обновлены не будут")
        ConsoleInputHandler.readEnterPress()
    }

    private fun readNewName(message: String,
                            defaultValue: String,
                            backCommand: String,
                            menuStorage: MenuItemStorage,
                            defaultIndicator: String = "-"): String?
    {
        var value: String? = null
        do {
            value = ConsoleInputHandler.readNotEmptyString(message)

            if (value == backCommand) {
                return null
            }
            if (value == defaultValue || value == defaultIndicator) {
                return defaultValue
            }
            if (menuStorage.getMenuItems().any{it.name == value}) {
                value = null
                OutputConsole.printMessage("В меню уже есть блюдо с таким названием!", OutputMessageType.Error)
            }
        } while (value == null)

        return value
    }

    private fun readNewPositiveInt(message: String, defaultValue: Int, backCommand: String, defaultIndicator: String = "-"): Int? {
        var number: Int?
        do {
            print(message)
            val numberStr = readln()
            if (numberStr == backCommand) {
                return null
            }
            if (numberStr == defaultIndicator) {
                return defaultValue
            }
            number = numberStr.toIntOrNull()
            if (number == null) {
                OutputConsole.printMessage("Неправильный формат ввода!", OutputMessageType.Error)
                continue
            }
            if (number <= 0) {
                number = null
                OutputConsole.printMessage("Число должно быть положительным!", OutputMessageType.Error)
            }
        } while (number == null)

        return number
    }
}