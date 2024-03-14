package org.example.commands.orders

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.entities.Order
import org.example.entities.OrderStatus
import org.example.entities.Review
import org.example.utils.ConsoleInputHandler
import org.example.utils.OutputConsole
import org.example.utils.OutputMessageType

class PayForOrderAppCommand(override var description: String = "Оплатить заказ") : AppCommand {
    override fun execute(argument: ApplicationCore) {

        val orderToPay = readOrder(argument) ?: return
        orderToPay.status = OrderStatus.Paid
        OutputConsole.printMessage("Заказ оплачен:")
        println(OutputConsole.displayOrderToVisitor(orderToPay, argument.userStorage))
        println()
        ConsoleInputHandler.readEnterPress()

        print("Хотели бы Вы оставить на блюда отзыв (Y/N): ")
        if (readln() != "Y") return

        if (readReview(orderToPay, argument)) {
            OutputConsole.printMessage("Отзыв(ы) сохранен(ы)", OutputMessageType.Success)
            ConsoleInputHandler.readEnterPress()
        }
    }

    private fun readOrder(argument: ApplicationCore): Order? {
        OutputConsole.displayOrders(argument.orderStorage.getUserOrders(argument.session.user!!.id), argument.userStorage)
        println()
        var orderToPay: Order? = null
        do {
            val id = ConsoleInputHandler.readIntCheckBackCommand("Введите номер заказа, который хотите оплатить: ", argument.backCommand) ?: return null
            orderToPay = argument.orderStorage.getOrder(id)
            if (orderToPay == null) {
                OutputConsole.printMessage("Заказа с таким номером нет!", OutputMessageType.Error)
                continue
            }
            if (orderToPay.status == OrderStatus.Paid) {
                if (orderToPay.userId != argument.session.user!!.id) {
                    OutputConsole.printMessage("Заказа с таким номером нет!", OutputMessageType.Error)
                    orderToPay = null
                    continue
                }
                OutputConsole.printMessage("Заказ уже оплачен")
                orderToPay = null
                continue
            }
            if (orderToPay.status != OrderStatus.Ready) {
                OutputConsole.printMessage("Оплатить можно только готовый заказ!", OutputMessageType.Error)
                orderToPay = null
                continue
            }

            if (orderToPay.userId != argument.session.user!!.id) {
                OutputConsole.printMessage("Выбранный Вами заказ принадлежит другому пользователю", OutputMessageType.Warning)
                print("Вы действительно хотите оплатить данный заказ (Y/N): ")
                if (readln() != "Y") {
                    orderToPay = null
                    continue
                }
            }
        } while (orderToPay == null)

        return orderToPay
    }

    private fun readReview(order: Order, argument: ApplicationCore): Boolean {
        val outputSet: MutableSet<String> = mutableSetOf()
        order.menuItems.forEach{outputSet.add("${it.id}. ${it.name}\n")}
        outputSet.forEach{ print(it) }

        var exit = false
        do {
            val itemId = ConsoleInputHandler.readIntCheckBackCommand("Введите номер блюда, на которое хотели" +
                    " бы оставить отзыв, или -1, чтобы завершить составление отзыва: ", argument.backCommand) ?: return false

            if (itemId == -1) {
                exit = true
                continue
            }

            val itemToReview = order.menuItems.firstOrNull{it.id == itemId}
            if (itemToReview == null){
                OutputConsole.printMessage("Блюда с таким номером нет в заказе!", OutputMessageType.Error)
                continue
            }

            val reviewSource = readMarkAndComment(argument) ?: return false
            argument.statisticsManager.reviewAdded(itemToReview, argument.session.user!!.login, reviewSource.first, reviewSource.second)
        } while (!exit)

        return true
    }

    private fun readMarkAndComment(argument: ApplicationCore): Pair<Int, String>? {
        var mark: Int? = null
        do {
            mark = ConsoleInputHandler.readIntCheckBackCommand("Введите оценку (число от ${Review.minimalMark} до ${Review.maxMark}): ", argument.backCommand) ?: return null

            if (mark < Review.minimalMark || mark > Review.maxMark) {
                OutputConsole.printMessage("Оценка не может быть меньше ${Review.minimalMark} и больше ${Review.maxMark}!", OutputMessageType.Error)
                mark = null
            }
        } while (mark == null)

        print("Введите комментарий к отзыву: ")
        val comment = readln()

        return if (comment == argument.backCommand) null else Pair(mark, comment)
    }
}