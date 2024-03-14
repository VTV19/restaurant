package org.example.commands.auth

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.crud.UserStorage
import org.example.utils.ConsoleInputHandler
import org.example.utils.OutputConsole
import org.example.utils.OutputMessageType

class RegisterAppCommand(override var description: String = "Регистрация аккаунта") : AppCommand{
    override fun execute(argument: ApplicationCore) {
        val login: String = readLogin(argument.userStorage, argument.backCommand) ?: return
        val password: String = readPassword(argument.backCommand) ?: return
        val isAdmin: Boolean = readIsAdmin(argument.backCommand) ?: return

        val user = argument.userStorage.createUser(login, password, isAdmin)
        OutputConsole.printMessage("Учетная запись ${user.login} создана!", OutputMessageType.Success)
        ConsoleInputHandler.readEnterPress()
    }

    private fun readLogin(userStorage: UserStorage, backCommand: String): String? {
        var login: String?
        do {
            login = ConsoleInputHandler.readNotEmptyString("Введите логин нового пользователя: ")
            if (login == backCommand) {
                return null
            }
            if (userStorage.loginExists(login)) {
                login = null
                OutputConsole.printMessage("Данный логин уже занят другим пользователем!", OutputMessageType.Error)
            }
        } while (login == null)

        return login
    }

    private fun readPassword(backCommand: String): String? {
        val passwords: Array<String> = arrayOf("", "")
        do {
            var hint = "Введите пароль: "
            for (i in 0..1) {
                passwords[i] = ConsoleInputHandler.readNotEmptyString(hint)
                hint = "Введите пароль (повторно): "

                if (passwords[i] == backCommand) {
                    return null
                }
            }

            if (passwords[0] != passwords[1]) {
                OutputConsole.printMessage("Пароль и его повтор не совпали!", OutputMessageType.Error)
            }
        } while (passwords[0] != passwords[1])

        return passwords[0]
    }

    private fun readIsAdmin(backCommand: String): Boolean? {
        print("Является ли пользователь администратором (Y/N): ")
        val input = readln()
        return if (input == backCommand) null else input == "Y"
    }
}