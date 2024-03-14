package org.example.entities

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.math.max

@Serializable
    class MenuItemStatistics private constructor(var id: Int,
                                                 val menuItemId: Int,
                                                 val reviews: MutableList<Review>,
                                                 private var _timesPurchased: Int,
                                                 private var _profit: Int,
                                                 private var _lastPurchaseDate: LocalDateTime?) {

    constructor(id: Int, menuItemId: Int): this(id, menuItemId, mutableListOf(),0, 0, null)

    var timesPurchased: Int
        get() = _timesPurchased
        set(value) {
            _timesPurchased = max(0, value)
        }

    var profit: Int
        get() = _profit
        set(value) {
            _profit = max(0, value)
        }

    var lastPurchaseDate: LocalDateTime?
        get() = _lastPurchaseDate
        set(value) {
            if (lastPurchaseDate == null) {
                _lastPurchaseDate = value
            }
            else {
                if (value == null) return

                if (_lastPurchaseDate!! < value) {
                    _lastPurchaseDate = value
                }
            }
        }
}