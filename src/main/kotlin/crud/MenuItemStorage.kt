package org.example.crud

import org.example.crud.Storage
import org.example.entities.MenuItem

interface MenuItemStorage: Storage {
    fun createMenuItem(name: String, quantity: Int, price: Int, timeToCook: Int): MenuItem

    fun addMenuItem(menuItem: MenuItem)

    fun getMenuItems(): Iterable<MenuItem>

    fun getMenuItem(id: Int): MenuItem?

    fun removeMenuItem(id: Int): Boolean
}