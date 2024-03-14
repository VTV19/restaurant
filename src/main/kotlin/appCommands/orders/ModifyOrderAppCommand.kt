package org.example.commands.orders

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.entities.MenuItem
import org.example.entities.Order
import org.example.entities.OrderStatus
import org.example.utils.ConsoleInputHandler
import org.example.utils.OutputConsole
import org.example.utils.OutputMessageType

class ModifyOrderAppCommand(override var description: String = "Редактировать заказ") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        OutputConsole.displayOrders(argument.orderStorage.getUserOrders(argument.session.user!!.id), argument.userStorage)
        println()

        val orderToChange = readOrder(argument) ?: return

        OutputConsole.displayMenu(argument.menuStorage.getMenuItems().filter { it.quantity > 0 }, argument.session.user!!.isAdmin)
        println()

        val orderItems: List<Pair<MenuItem, Int>>? = ConsoleInputHandler.readMenuItems(argument.menuStorage, argument.backCommand)
        if (orderItems.isNullOrEmpty()) {
            orderToChange.releaseOwnership()
            return
        }
        orderItems.forEach {
            orderToChange.addMenuItem(it.first, it.second)
        }

        orderToChange.releaseOwnership()

        argument.statisticsManager.orderUpdated(orderItems.map { it.first })

        OutputConsole.printMessage("Заказ обновлен", OutputMessageType.Success)
        println("Ваш заказ:")
        println(OutputConsole.displayOrderToVisitor(orderToChange, argument.userStorage))
        println()
        ConsoleInputHandler.readEnterPress()
    }

    private fun readOrder(argument: ApplicationCore): Order? {
        var orderToChange: Order? = null
        do {
            val id = ConsoleInputHandler.readIntCheckBackCommand("Введите номер заказа, в который хотели бы добавить блюдо: ", argument.backCommand) ?: return null
            orderToChange = argument.orderStorage.getOrder(id)
            if (orderToChange == null) {
                OutputConsole.printMessage("Нет заказа с таким номером!", OutputMessageType.Error)
                continue
            }
            if (orderToChange.userId != argument.session.user!!.id) {
                orderToChange = null
                OutputConsole.printMessage("Нет прав модификации данного заказа!", OutputMessageType.Error)
                continue
            }

            orderToChange.requestOwnership()
            if (orderToChange.status != OrderStatus.Created && orderToChange.status != OrderStatus.OnCook) {
                orderToChange.releaseOwnership()
                orderToChange = null
                OutputConsole.printMessage("Нельзя изменять заказ после того, как он готов!", OutputMessageType.Error)
                continue
            }
        } while (orderToChange == null)

        return orderToChange
    }
}