package org.example.commands.auth

import org.example.ApplicationCore
import org.example.commands.AppCommand

class ExitAppCommand(override var description: String = "Завершить работу") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        argument.exitRequired = true
        argument.state = null
    }
}