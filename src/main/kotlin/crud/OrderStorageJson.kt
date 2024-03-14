package org.example.crud

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.entities.User
import org.example.entities.MenuItem
import org.example.entities.Order
import org.example.entities.Event
import java.io.File
import java.io.IOException
import java.util.Random

class OrderStorageJson(private val sourcePath: String): OrderStorage {
    private var _orders: MutableMap<Int, Order> = mutableMapOf()
    private var _nextId: Int = 0
    private val _random: Random = Random()

    override val orderCreated: Event<Order> = Event<Order>()

    override fun initialize() {
        _orders = try {
            Json.decodeFromString(File(sourcePath).readText())
        } catch (e: IOException) {
            mutableMapOf()
        }
        catch (e: SerializationException) {
            mutableMapOf()
        }

        if (_orders.any()) {
            _nextId = _orders.maxOf { it.value.id } + 1
        }
    }

    override fun destruct() {
        save()
    }

    override fun getOrders(): Iterable<Order> = _orders.values

    override fun getUserOrders(userId: Int): Iterable<Order> = _orders.values.filter { it.userId == userId }

    override fun getOrder(id: Int): Order? = _orders[id]

    override fun createOrder(user: User, menuItems: List<MenuItem>): Order {
        val order = Order(_nextId++, user, menuItems)
        order.priority = order.totalPrice
        _orders[order.id] = order
        save()
        orderCreated(order)
        return order
    }

    override fun removeOrder(id: Int): Boolean {
        if (!_orders.containsKey(id)) {
            return false
        }

        _orders.remove(id)
        save()
        return true
    }

    override fun addOrder(order: Order) {
        order.id = _nextId++
        _orders[order.id] = order
        save()
    }

    private fun save() {
        try {
            File(sourcePath).writeText(Json.encodeToString(_orders))
        }
        catch (e: IOException) {
            return
        }
        catch (e: SerializationException) {
            return
        }
    }
}