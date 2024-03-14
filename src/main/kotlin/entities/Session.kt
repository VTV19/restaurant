package org.example.entities

import kotlinx.datetime.*
import org.example.utils.LocalDateTimeEnhancements.Companion.now

class Session {
    private var _user: User? = null
    private var _dateOfAuthorization: LocalDateTime? = null

    var user: User?
        get() = _user
        private set(value) {
            _user = value
        }

    var dateOfAuthorization: LocalDateTime?
        get() = _dateOfAuthorization
        private set(value) {
            _dateOfAuthorization = value
        }

    fun authorizeUser(userToAuthorize: User, password: String): Boolean {
        if (!userToAuthorize.checkPassword(password)) {
            return false
        }
        user = userToAuthorize
        dateOfAuthorization = LocalDateTime.now()
        return true
    }

    fun deAuthorizeUser(){
        user = null
        dateOfAuthorization = null
    }
}