package org.example

import org.example.crud.UserStorageJson
import org.example.crud.MenuItemStorageJson
import org.example.crud.OrderStorageJson
import org.example.crud.MenuItemStatisticsStorageJson
import org.example.states.InitialState
import org.example.utils.PropertyManager
import org.example.utils.OutputConsole

fun main() {
    val application =  ApplicationCore(
        PropertyManager.get("backCommand"),
        UserStorageJson(PropertyManager.get("userStoragePath")),
        MenuItemStorageJson(PropertyManager.get("menuStoragePath")),
        OrderStorageJson(PropertyManager.get("orderStoragePath")),
        PropertyManager.get("cookingWorkersCount").toInt(),
        MenuItemStatisticsStorageJson(PropertyManager.get("statisticsStoragePath"))
    )
    application.state = InitialState(application)
    application.initialize()

    try {
        while (!application.exitRequired) {
            application.process()
        }
    }
    catch (e: Exception) {
        OutputConsole.printMessage("Во время работы произощла ошика, перезапустите программу1")
    }

    application.destruct()
}