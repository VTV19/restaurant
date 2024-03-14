package org.example.entities

class CookingTask (val order: Order) {
    val orderFinished: Event<Order> = Event<Order>()

    fun finishTask() {
        order.status = OrderStatus.Ready
        orderFinished(order)
    }
}