package org.example.crud

import org.example.crud.Storage
import org.example.entities.MenuItem
import org.example.entities.MenuItemStatistics

interface MenuItemStatisticsStorage: Storage {

    fun createStatistics(menuItem: MenuItem): MenuItemStatistics

    fun getStatistics(menuItemId: Int): MenuItemStatistics?

    fun removeStatistics(menuItemId: Int): Boolean

    fun save()
}