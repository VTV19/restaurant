package org.example.commands.orders

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.utils.ConsoleInputHandler
import org.example.utils.OutputConsole

class ShowOrdersAppCommand(override var description: String = "Просмотреть заказы"): AppCommand {
    override fun execute(argument: ApplicationCore) {
        val isAdmin = argument.session.user!!.isAdmin

        val orders =
            if (isAdmin) argument.orderStorage.getOrders()
            else argument.orderStorage.getUserOrders(argument.session.user!!.id)
        OutputConsole.displayOrders(orders, argument.userStorage, argument.session.user!!.isAdmin)
        println()
        ConsoleInputHandler.readEnterPress()
    }
}