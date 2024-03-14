package org.example.commands.orders

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.entities.Order
import org.example.entities.OrderStatus
import org.example.utils.ConsoleInputHandler
import org.example.utils.OutputConsole
import org.example.utils.OutputMessageType

class CancelOrderAppCommand(override var description: String = "Отменить заказ") : AppCommand {
    override fun execute(argument: ApplicationCore) {
        val isAdmin = argument.session.user!!.isAdmin

        val statusFilter: (Order) -> Boolean = {it.status == OrderStatus.Created || it.status == OrderStatus.OnCook}
        val orders =
            if (isAdmin) argument.orderStorage.getOrders().filter(statusFilter)
            else argument.orderStorage.getUserOrders(argument.session.user!!.id).filter(statusFilter)

        OutputConsole.displayOrders(orders, argument.userStorage, argument.session.user!!.isAdmin)
        println()

        var orderToCancel: Order? = null
        do {
            val id = ConsoleInputHandler.readIntCheckBackCommand("Введите номер заказа, который хотите отменить: ", argument.backCommand) ?: return

            orderToCancel = argument.orderStorage.getOrder(id)
            if (orderToCancel == null) {
                OutputConsole.printMessage("Заказа с таким номером нет!", OutputMessageType.Error)
                continue
            }

            if (orderToCancel.userId != argument.session.user!!.id && !argument.session.user!!.isAdmin) {
                OutputConsole.printMessage("У вас нет прав на отмену данного заказа!")
                orderToCancel = null
                continue
            }

            orderToCancel.requestOwnership()
            if (orderToCancel.status != OrderStatus.Created && orderToCancel.status != OrderStatus.OnCook) {
                OutputConsole.printMessage("Нельзя отменить заказ после того, как он готов!", OutputMessageType.Error)
                orderToCancel.releaseOwnership()
                orderToCancel = null
                continue
            }
        } while (orderToCancel == null)

        orderToCancel.status = OrderStatus.Cancelled
        orderToCancel.releaseOwnership()
        argument.statisticsManager.orderCancelled(orderToCancel)
        OutputConsole.printMessage("Заказ отменён", OutputMessageType.Success)
        ConsoleInputHandler.readEnterPress()
    }

}