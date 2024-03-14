package org.example.commands.navigation

import org.example.ApplicationCore
import org.example.commands.AppCommand

class BackAppCommand(override var description: String = "Назад") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        argument.state = argument.state?.previousState
    }
}