package org.example.utils

import org.example.entities.OrderStatus

class OrderStatusEnhancer {
    companion object {
        fun OrderStatus.toBeautifulString(): String{
            return when (this) {
                OrderStatus.Created -> "В очереди"
                OrderStatus.OnCook -> "Готовится"
                OrderStatus.Ready -> "Готов"
                OrderStatus.Cancelled -> "Отменен"
                OrderStatus.Paid -> "Оплачен"
                else -> ""
            }
        }
    }
}