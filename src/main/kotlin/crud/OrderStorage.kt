package org.example.crud

import org.example.entities.User
import org.example.entities.MenuItem
import org.example.entities.Order
import org.example.entities.Event

interface OrderStorage: Storage {
    fun getOrders(): Iterable<Order>

    fun getUserOrders(userId: Int): Iterable<Order>

    fun getOrder(id: Int): Order?

    fun createOrder(user: User, menuItems: List<MenuItem>): Order

    fun addOrder(order: Order)

    fun removeOrder(id: Int): Boolean

    val orderCreated: Event<Order>
}