package org.example.utils

import org.example.crud.UserStorage
import org.example.entities.Session
import org.example.entities.MenuItem
import org.example.entities.Order
import org.example.utils.LocalDateTimeEnhancements.Companion.toBeautifulString
import org.example.utils.OrderStatusEnhancer.Companion.toBeautifulString

class OutputConsole {
    companion object {
        fun printMessage(message: String, messageType: OutputMessageType = OutputMessageType.Warning) {
            val coloredMessage: String = when (messageType) {
                OutputMessageType.Error -> "Ошибка! $message"
                OutputMessageType.Warning -> "Внимание! $message"
                OutputMessageType.Success -> "Успешно: $message"
                else -> message
            }

            println(coloredMessage)
        }

        fun clearConsole() {
            println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
        }

        fun displayMenu(menu: Iterable<MenuItem>, isAdmin: Boolean) {
            println("================================  \uD83C\uDF7D\uFE0F Меню ================================")
            if (!menu.any()) {
                println("В меню не добавлено ни одно блюдо")
                return
            }

            var presenter: (MenuItem) -> String = if (isAdmin) Companion::displayMenuItemToAdmin else Companion::displayMenuItemToVisitor;

            menu.forEach{ "||" + println(presenter(it)) + "||"}
            println("==========================================================================================")
        }

        fun displayOrders(orders: Iterable<Order>, userStorage: UserStorage, isAdmin: Boolean = false) {

            var presenter: (Order, UserStorage) -> String = if (isAdmin) Companion::displayOrderToAdmin else Companion::displayOrderToVisitor;

            println("\n\n\n")
            println(if (isAdmin) "Заказы" else "Мои заказы")
            println("=========================================================================================")
            val ordersToShow = orders.sortedBy { it.date }.asReversed()
            ordersToShow.forEach{ presenter(it, userStorage) }
        }

        fun displayOrderToVisitor(order: Order, userStorage: UserStorage): String {
            val header = "— ${"${order.id}"} —\n" + "Заказ от ${order.date.toBeautifulString()}:\n"
            val ordersBody = order.menuItems
                .map{Pair(it.name, it.price)}
                .joinToString(separator = "\n-\t", prefix = "-\t") {it.first + " ........ " + it.second}
            val ending = "\nИтого: ${order.totalPrice} \uD83E\uDE99  \nСтатус: ${order.status.toBeautifulString()}\n"

            return header + ordersBody + ending
        }

        fun displayOrderToAdmin(order: Order, userStorage: UserStorage): String {
            val header = "— ${"${order.id}"} —\n" + "Заказ от пользователя ${userStorage.getUser(order.userId)?.login ?: ""} " +
                    "(${order.date.toBeautifulString()}): \n"
            val ordersBody = "[${order.menuItems.map{it.name}.joinToString(", "){ it }}]"
            val ending = "\nСтоимость заказа: ${order.totalPrice}, время приготовления: ${order.timeToCook}" +
                    "\nСтатус: ${order.status.toBeautifulString()}\n"

            return header + ordersBody + ending
        }

        fun displayMenuItemToAdmin(menuItem: MenuItem): String {
            return "${menuItem.id}. ${menuItem.name}: порций: ${menuItem.quantity}," +
                    " цена: ${menuItem.price}, время готовки: ${menuItem.timeToCook} мин."
        }
        fun displayMenuItemToVisitor(menuItem: MenuItem): String {
            return "${menuItem.id}. ${menuItem.name} ........... ${menuItem.price}"
        }

        fun printSessionUser(session: Session) {
            if (session.user == null){
                return
            }

            val userType = if (session.user!!.isAdmin) "Сотрудник" else "Посетитель"

            println("=========================================================================================")
            println("\uD83D\uDC64 ${session.user!!.login} ($userType), время входа: ${session.dateOfAuthorization!!.toBeautifulString()}")
            println("=========================================================================================")
        }
    }
}

enum class OutputMessageType {Error, Warning, Success}