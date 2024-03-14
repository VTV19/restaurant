package org.example.commandFactories

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.commands.navigation.BackAppCommand
import org.example.commands.navigation.OrdersAppCommand

class MainMenuStateVisitorAppCommandFactory: AppCommandFactory<ApplicationCore> {
    override fun createCommandSet(): Iterable<AppCommand> {
        return listOf(OrdersAppCommand(), BackAppCommand())
    }
}