package org.example.crud

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.entities.MenuItem
import org.example.entities.MenuItemStatistics
import java.io.File
import java.io.IOException

class MenuItemStatisticsStorageJson(private val sourcePath: String): MenuItemStatisticsStorage {
    private var _statistics: MutableMap<Int, MenuItemStatistics> = mutableMapOf()
    private var _nextId = 0
    override fun initialize() {
        _statistics = try {
            Json.decodeFromString(File(sourcePath).readText())
        } catch (e: IOException) {
            mutableMapOf()
        }
        catch (e: SerializationException) {
            mutableMapOf()
        }

        if (_statistics.any()) {
            _nextId = _statistics.maxOf { it.value.id } + 1
        }
    }

    override fun destruct() = save()

    override fun createStatistics(menuItem: MenuItem): MenuItemStatistics {
        val statistics = MenuItemStatistics(_nextId++, menuItem.id)
        _statistics[menuItem.id] = statistics
        save()
        return statistics
    }

    override fun getStatistics(menuItemId: Int): MenuItemStatistics? = _statistics[menuItemId]

    override fun removeStatistics(menuItemId: Int): Boolean {
        if (!_statistics.containsKey(menuItemId)) {
            return false
        }
        _statistics.remove(menuItemId)
        save()
        return true
    }

    override fun save() {
        try {
            File(sourcePath).writeText(Json.encodeToString(_statistics))
        }
        catch (e: IOException) {
            return
        }
        catch (e: SerializationException) {
            return
        }
    }
}