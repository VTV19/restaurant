package org.example.commandFactories

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.commands.navigation.ManageMenuAppCommand
import org.example.commands.navigation.MenuStatisticsAppCommand
import org.example.commands.navigation.BackAppCommand
import org.example.commands.navigation.OrdersAppCommand

class MainMenuStateAdminAppCommandFactory: AppCommandFactory<ApplicationCore> {
    override fun createCommandSet(): Iterable<AppCommand> {
        return listOf(ManageMenuAppCommand(), OrdersAppCommand(), MenuStatisticsAppCommand(), BackAppCommand())
    }
}