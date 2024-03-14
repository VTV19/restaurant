package org.example.commands.orders

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.entities.MenuItem
import org.example.utils.ConsoleInputHandler
import org.example.utils.OutputConsole
import org.example.utils.OutputMessageType

class CreateOrderAppCommand(override var description: String = "Создать заказ") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        OutputConsole.displayMenu(argument.menuStorage.getMenuItems().filter { it.quantity > 0 }, argument.session.user!!.isAdmin)
        println()
        val orderItems: List<Pair<MenuItem, Int>> = ConsoleInputHandler.readMenuItems(argument.menuStorage, argument.backCommand) ?: return
        if (orderItems.isEmpty()) {
            OutputConsole.printMessage("Нельзя создать заказ без блюд!", OutputMessageType.Error)
            ConsoleInputHandler.readEnterPress()
            return
        }
        val menuItems: MutableList<MenuItem> = mutableListOf()
        orderItems.forEach {
            it.first.quantity -= it.second
            for (i in 1..it.second) {
                menuItems.addLast(it.first)
            }
        }

        val order = argument.orderStorage.createOrder(argument.session.user!!, menuItems)
        argument.statisticsManager.orderCreated(order)

        println("Ваш заказ: ")
        println(OutputConsole.displayOrderToVisitor(order, argument.userStorage))
        println()
        ConsoleInputHandler.readEnterPress()
    }
}