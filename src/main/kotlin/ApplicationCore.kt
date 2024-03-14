package org.example

import org.example.states.AppState
import org.example.crud.UserStorage
import org.example.crud.MenuItemStorage
import org.example.crud.OrderStorage
import org.example.entities.Session
import org.example.entities.CookingManager
import org.example.entities.CookingTask
import org.example.crud.MenuItemStatisticsStorage
import org.example.entities.OrderStatus
import org.example.entities.StatisticsManager
import org.example.utils.OutputConsole

class ApplicationCore (
    var backCommand: String,

    var userStorage: UserStorage,

    val menuStorage: MenuItemStorage,

    val orderStorage: OrderStorage,

    workersCount: Int,

    statisticsStorage: MenuItemStatisticsStorage
) {

    val session = Session()

    var state: AppState? = null

    var exitRequired: Boolean = false

    val cookingManager: CookingManager = CookingManager(workersCount)

    val statisticsManager: StatisticsManager =  StatisticsManager(statisticsStorage)

    fun process() {
        OutputConsole.clearConsole()
        OutputConsole.printSessionUser(session)
        if (state == null) {
            exitRequired = true;
            return
        }

        state!!.process()
    }

    fun initialize() {
        userStorage.initialize()
        menuStorage.initialize()
        orderStorage.initialize()
        statisticsManager.statisticsStorage.initialize()

        orderStorage.getOrders()
            .filter { it.status == OrderStatus.Created || it.status == OrderStatus.OnCook }
            .forEach{cookingManager.addTask(CookingTask(it))}
        orderStorage.orderCreated += {cookingManager.addTask(CookingTask(it))}

        cookingManager.startWorking()
    }

    fun destruct() {
        cookingManager.stopWorking()
        cookingManager.stopWorking()
        userStorage.destruct()
        menuStorage.destruct()
        orderStorage.destruct()
        statisticsManager.statisticsStorage.destruct()
    }
}