package org.example.crud

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.entities.MenuItem
import java.io.File
import java.io.IOException

class MenuItemStorageJson(private val sourcePath: String): MenuItemStorage {
    private var _menuItems: MutableMap<Int, MenuItem> = mutableMapOf()
    private var _nextId: Int = 0

    override fun initialize() {
        _menuItems = try {
            Json.decodeFromString(File(sourcePath).readText())
        } catch (e: IOException) {
            mutableMapOf()
        }
        catch (e: SerializationException) {
            mutableMapOf()
        }

        if (_menuItems.any()) {
            _nextId = _menuItems.maxOf { it.value.id } + 1
        }
    }

    override fun destruct() {
        save()
    }
    override fun createMenuItem(name: String, quantity: Int, price: Int, timeToCook: Int): MenuItem {
        val newItem = MenuItem(_nextId++, name, quantity, price, timeToCook)
        _menuItems[newItem.id] = newItem

        return newItem
    }

    override fun addMenuItem(menuItem: MenuItem) {
        menuItem.id = _nextId++
        _menuItems[menuItem.id] = menuItem
    }

    override fun getMenuItems(): Iterable<MenuItem> = _menuItems.values

    override fun getMenuItem(id: Int): MenuItem? = _menuItems[id]

    override fun removeMenuItem(id: Int): Boolean {
        if (!_menuItems.containsKey(id)) {
            return false
        }

        _menuItems.remove(id)
        return true
    }

    private fun save() {
        try {
            File(sourcePath).writeText(Json.encodeToString(_menuItems))
        }
        catch (e: IOException) {
            return
        }
        catch (e: SerializationException) {
            return
        }
    }
}