package org.example.commands.auth

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.entities.User
import org.example.states.InitialState
import org.example.utils.ConsoleInputHandler
import org.example.utils.OutputConsole
import org.example.utils.OutputMessageType

class DeleteAccountAppCommand(override var description: String = "Удалить аккаунт") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        val userToDelete: User = ConsoleInputHandler.readExistingUserByLogin(argument.userStorage, argument.backCommand)
            ?: return

        ConsoleInputHandler.readPasswordForUser(userToDelete, argument.backCommand) ?: return

        if (!argument.userStorage.removeUser(userToDelete.id)) {
            OutputConsole.printMessage("Не удалось удалить учетную запись ${userToDelete.login}", OutputMessageType.Error)
            ConsoleInputHandler.readEnterPress()
            return
        }

        if (argument.session.user == userToDelete) {
            argument.session.deAuthorizeUser()
            argument.state = InitialState(argument)
        }

        OutputConsole.printMessage("Учетная запись ${userToDelete.login} удалена", OutputMessageType.Success)
        ConsoleInputHandler.readEnterPress()
    }
}