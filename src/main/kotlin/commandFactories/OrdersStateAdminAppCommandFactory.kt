package org.example.commandFactories

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.commands.navigation.BackAppCommand
import org.example.commands.orders.CancelOrderAppCommand
import org.example.commands.orders.ShowOrdersAppCommand

class OrdersStateAdminAppCommandFactory: AppCommandFactory<ApplicationCore> {
    override fun createCommandSet(): Iterable<AppCommand> {
        return listOf(ShowOrdersAppCommand(), CancelOrderAppCommand(), BackAppCommand())
    }
}