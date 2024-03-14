package org.example.entities

import kotlinx.datetime.LocalDateTime
import org.example.crud.MenuItemStatisticsStorage
import org.example.utils.LocalDateTimeEnhancements.Companion.now

class StatisticsManager(val statisticsStorage: MenuItemStatisticsStorage) {
    fun menuItemAdded(menuItem: MenuItem) {
        if (statisticsStorage.getStatistics(menuItem.id) != null) {
            return
        }

        statisticsStorage.createStatistics(menuItem)
    }

    fun menuItemRemoved(menuItem: MenuItem) = statisticsStorage.removeStatistics(menuItem.id)

    fun orderCreated(order: Order) {
        menuItemsOrdered(order.menuItems)
    }

    fun orderUpdated(update: Iterable<MenuItem>) = menuItemsOrdered(update)

    fun orderCancelled(order: Order) {
        order.menuItems.forEach {
            val statistics = statisticsStorage.getStatistics(it.id)
            if (statistics != null) {
                statistics.profit -= it.price
                statistics.timesPurchased -= 1
            }
        }
        statisticsStorage.save()
    }

    fun reviewAdded(menuItem: MenuItem, author: String, mark: Int, message: String) {
        var statistics = statisticsStorage.getStatistics(menuItem.id)
        if (statistics == null) {
            statistics = statisticsStorage.createStatistics(menuItem)
        }

        statistics.reviews.add(Review(author, mark, message, LocalDateTime.now()))

        statisticsStorage.save()
    }

    private fun menuItemsOrdered(menuItems: Iterable<MenuItem>) {
        for (menuItem in menuItems) {
            var statistics = statisticsStorage.getStatistics(menuItem.id)
            if (statistics == null) {
                statistics = statisticsStorage.createStatistics(menuItem)
            }

            statistics.timesPurchased++
            statistics.profit += menuItem.price
            statistics.lastPurchaseDate = LocalDateTime.now()
        }
        statisticsStorage.save()
    }
}