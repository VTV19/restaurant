package org.example.commands.auth

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.states.InitialState

class LogoutAppCommand(override var description: String = "Выйти из аккаунта") : AppCommand{
    override fun execute(argument: ApplicationCore) {
        print("Выйти из учётной записи (Y/N): ")
        if (readln() == "Y") {
            argument.session.deAuthorizeUser()
            println("Выполнен выход из учётной записи")
            argument.state = InitialState(argument)
        }
    }
}