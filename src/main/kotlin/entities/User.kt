package org.example.entities
import kotlinx.serialization.Serializable

@Serializable
class User private constructor(val id: Int, val login: String, private var _passwordHash: Int, val isAdmin: Boolean) {
    constructor(id: Int, login: String, password: String, isAdmin: Boolean):
            this(id, login, password.hashCode(), isAdmin){}

    init {
        require(login.trim().isNotEmpty()) {"Логин не может быть пустым."}
    }

    fun checkPassword(password: String): Boolean = password.hashCode() == _passwordHash

    fun changePassword(oldPassword: String, newPassword: String): Boolean {
        if (!checkPassword(oldPassword)) {
            return false
        }
        _passwordHash = newPassword.hashCode()
        return true
    }

    override fun toString(): String {
        return "$login (${ if (isAdmin) "(Администратор)" else "Посетитель"})"
    }
}