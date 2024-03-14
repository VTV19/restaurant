package org.example.states

import org.example.ApplicationCore
import org.example.utils.LocalDateTimeEnhancements.Companion.toBeautifulString
import org.example.utils.ConsoleInputHandler

class MenuStatisticsState(application: ApplicationCore, previousState: AppState?): AppState(application, previousState) {
    override fun process() {
        var totalProfit = 0
        var totalPurchases = 0

        println("Статистика по блюдам: ")
        for (menuItem in application.menuStorage.getMenuItems()) {
            println("— ${menuItem.id} ${menuItem.name} —")
            val statistics = application.statisticsManager.statisticsStorage.getStatistics(menuItem.id)
            if (statistics == null) {
                println("\t Нет данных.")
                continue
            }
            println("\t Количетство купленных порций: ${statistics.timesPurchased}")
            println("\t Прибыль от продажи блюда: ${statistics.profit}")
            if (statistics.lastPurchaseDate != null) {
                println("\t Дата и время последнего приобретения: ${statistics.lastPurchaseDate!!.toBeautifulString()}")
            }
            if (statistics.reviews.isNotEmpty()) {
                println("\t Средняя оценка блюда: ${statistics.reviews.map { it.mark }.average()}")
                println("\t Отзывы:")
                statistics.reviews.forEach{println("\t\t$it")}
            }
            println()
            totalProfit += statistics.profit
            totalPurchases += statistics.timesPurchased
        }
        println()
        println("======================================================================================================")
        println("Общая прибыль: $totalProfit, всего блюд продано: $totalPurchases")


        ConsoleInputHandler.readEnterPress()
        application.state = previousState
    }
}