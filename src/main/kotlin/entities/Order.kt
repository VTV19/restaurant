package org.example.entities

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.example.utils.LocalDateTimeEnhancements.Companion.now
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

@Serializable
class Order private constructor(var id: Int,
                                val userId: Int,
                                private var _menuItems: MutableList<MenuItem>,
                                var status: OrderStatus,
                                var priority: Int,
                                private var _totalPrice: Int,
                                private var _timeToCook: Int,
                                val date: LocalDateTime) {
    @kotlinx.serialization.Transient
    private val _lock: Lock = ReentrantLock()

    constructor(id: Int,
                user: User,
                menuItems: Iterable<MenuItem>,
                priority: Int = 0,
                date: LocalDateTime = LocalDateTime.now())
            : this (id, user.id, menuItems.toMutableList(),
        OrderStatus.Created, priority, menuItems.sumOf { it.price }, menuItems.sumOf { it.timeToCook }, date)

    val totalPrice: Int
        get() = _totalPrice

    val timeToCook: Int
        get() = _timeToCook

    val menuItems: List<MenuItem>
        get() = _menuItems.toList()


    fun addMenuItem(menuItem: MenuItem, portions: Int = 1) {
        require(portions > 0) {"Количество порций должно быть положительным"}
        menuItem.quantity -= portions

        if (status != OrderStatus.Created && status != OrderStatus.OnCook) {
            throw Exception("Нельзя модифицировать данный заказ!")
        }

        for (i in 1..portions) {
            _menuItems.add(menuItem)
        }

        _totalPrice += menuItem.price * portions
        _timeToCook += menuItem.timeToCook * portions
        priority += menuItem.price * portions
    }

    fun requestOwnership() = _lock.lock()

    fun releaseOwnership() = _lock.unlock()
}