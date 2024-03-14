package org.example.commands.navigation

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.states.MainMenuState

class MainMenuAppCommand(override var description: String = "Главное меню") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        argument.state = MainMenuState(argument, argument.state)
    }
}