package org.example.commands.navigation

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.states.ManageMenuState

class ManageMenuAppCommand(override var description: String = "Посмотреть меню") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        argument.state = ManageMenuState(argument, argument.state)
    }
}