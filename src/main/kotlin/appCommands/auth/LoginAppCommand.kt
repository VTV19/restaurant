package org.example.commands.auth

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.entities.User
import org.example.states.MainMenuState
import org.example.utils.ConsoleInputHandler
import org.example.utils.OutputConsole
import org.example.utils.OutputMessageType

class LoginAppCommand(override var description: String = "Войти") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        if (argument.session.user != null) {
            println("В системе уже есть авторизованный пользователь. Чтобы начать новую сессию, закончите текущую.")
            argument.state = MainMenuState(argument, argument.state)
            ConsoleInputHandler.readEnterPress()
            return
        }

        val userToLogin: User =
            ConsoleInputHandler.readExistingUserByLogin(argument.userStorage, argument.backCommand) ?: return

        val password: String = ConsoleInputHandler.readPasswordForUser(userToLogin, argument.backCommand) ?: return

        if (!argument.session.authorizeUser(userToLogin, password)) {
            OutputConsole.printMessage("Во время авторизации произошла ошибка!", OutputMessageType.Error)
            ConsoleInputHandler.readEnterPress()
            return
        }

        OutputConsole.printMessage(
            "Успешная авторизация пользователя ${argument.session.user!!.login}",
            OutputMessageType.Success)
        println()
        ConsoleInputHandler.readEnterPress()

        argument.state = MainMenuState(argument, argument.state)
    }
}