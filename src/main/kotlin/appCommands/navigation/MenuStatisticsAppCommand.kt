package org.example.commands.navigation

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.states.MenuStatisticsState

class MenuStatisticsAppCommand(override var description: String = "Просмотреть статистику блюд") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        argument.state = MenuStatisticsState(argument, argument.state)
    }
}