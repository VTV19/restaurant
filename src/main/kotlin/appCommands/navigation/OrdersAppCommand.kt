package org.example.commands.navigation

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.states.OrdersState

class OrdersAppCommand(override var description: String = "Просмотреть заказы") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        argument.state = OrdersState(argument, argument.state)
    }
}