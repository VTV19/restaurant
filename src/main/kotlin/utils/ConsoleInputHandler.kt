package org.example.utils

import org.example.crud.UserStorage
import org.example.crud.MenuItemStorage
import org.example.entities.User
import org.example.entities.MenuItem

class ConsoleInputHandler {
    companion object {
        fun readEnterPress(message: String = "Для продолжения нажмите Enter") {
            println(message)
            readln()
        }

        fun readNotEmptyString(message: String): String {
            var str = ""
            do {
                print(message)
                str = readln()
                if (str.trim().isBlank()) {
                    OutputConsole.printMessage(
                        "Строка не может быть пустой, повторите ввод!",
                        OutputMessageType.Error
                    )
                    str = ""
                }
            } while (str == "")

            return str
        }

        fun readInt(message: String): Int {
            var number: Int? = null
            do {
                print(message)
                val numStr = readln()

                number = numStr.toIntOrNull()
                if (number == null) {
                    OutputConsole.printMessage("Неправильный формат ввода!", OutputMessageType.Error)
                }
            } while (number == null)

            return number
        }

        fun readIntCheckBackCommand(message: String, backCommand: String): Int? {
            var number: Int? = null

            do {
                print(message)
                val numStr = readln()
                if (numStr == backCommand) {
                    return null
                }

                number = numStr.toIntOrNull()
                if (number == null) {
                    OutputConsole.printMessage("Неправильный формат ввода!", OutputMessageType.Error)
                }
            } while (number == null)

            return number
        }

        fun readMenuItems(menuStorage: MenuItemStorage, backCommand: String): List<Pair<MenuItem, Int>>? {
            val items: MutableList<Pair<MenuItem, Int>> = mutableListOf()

            do {
                val menuItemId: Int = readIntCheckBackCommand("Введите номер блюда, которое хотели" +
                        " бы добавить в заказ, либо -1, чтобы прекратить ввод: ", backCommand) ?: return null

                if (menuItemId == -1) {
                    break
                }

                val menuItem = menuStorage.getMenuItem(menuItemId)
                if (menuItem == null) {
                    OutputConsole.printMessage("Блюда с таким номером нет в меню!", OutputMessageType.Error)
                    continue
                }
                if (menuItem.quantity == 0) {
                    OutputConsole.printMessage("Данное блюдо недоступно!", OutputMessageType.Error)
                    continue
                }

                OutputConsole.printMessage("Доступно ${menuItem.quantity} порций блюда")

                val num = readPortionsNumber(menuItem, backCommand) ?: return null
                items.addLast(Pair(menuItem, num))
            } while (menuItemId != -1)

            return items
        }

        private fun readPortionsNumber(menuItem: MenuItem, backCommand: String): Int? {
            var portions: Int? = null
            do {
                portions = readIntCheckBackCommand("Введите количество порций: ", backCommand) ?: return null

                if (portions <= 0) {
                    OutputConsole.printMessage(
                        "Количество порций должно быть положительным!",
                        OutputMessageType.Error
                    )
                    portions = null
                    continue
                }

                if (portions > menuItem.quantity) {
                    OutputConsole.printMessage(
                        "Доступно всего ${menuItem.quantity} порций!",
                        OutputMessageType.Error
                    )
                    portions = null
                    continue
                }
            } while (portions == null)

            return portions
        }

        fun readExistingUserByLogin(userStorage: UserStorage, backCommand: String): User? {
            var user: User? = null

            do {
                val login = readNotEmptyString("Введите логин: ")
                if (login == backCommand) {
                    return null
                }

                if (userStorage.loginExists(login)) {
                    user = userStorage.getUser(login)
                    continue
                }

                OutputConsole.printMessage("Пользователя с таким логином нет!", OutputMessageType.Error)
            } while (user == null)

            return user
        }

        fun readPasswordForUser(user: User, backCommand: String): String? {
            var password: String? = null

            do {
                password = readNotEmptyString("Введите пароль: ")
                if (password == backCommand) {
                    return null
                }
                if (user.checkPassword(password)) {
                    continue
                }
                password = null
                OutputConsole.printMessage("Неправильный пароль!", OutputMessageType.Error)
            } while (password == null)

            return password
        }
    }
}