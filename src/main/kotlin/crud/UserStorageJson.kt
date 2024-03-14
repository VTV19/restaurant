package org.example.crud

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.entities.User
import java.io.File
import java.io.IOException

class UserStorageJson(val sourcePath: String): UserStorage {
    private var _users: MutableMap<Int, User> = mutableMapOf()
    private var _nextId: Int = 0

    override fun initialize() {
        _users = try {
            Json.decodeFromString(File(sourcePath).readText())
        } catch (e: IOException) {
            mutableMapOf()
        }
        catch (e: SerializationException) {
            mutableMapOf()
        }

        if (_users.any()) {
            _nextId = _users.maxOf { it.value.id } + 1
        }
    }

    override fun destruct() {
        save()
    }

    override fun loginExists(login: String): Boolean = _users.any { it.value.login == login }

    override fun createUser(login: String, password: String, isAdmin: Boolean): User {
        validateLogin(login)

        val newUser = User(_nextId++, login, password, isAdmin)
        _users[newUser.id] = newUser

        save()
        return newUser
    }

    override fun getUser(login: String): User? = _users.values.firstOrNull{it.login == login}
    override fun getUser(id: Int): User? = _users[id]

    override fun removeUser(id: Int): Boolean {
        if (!_users.containsKey(id)) {
            return false
        }
        _users.remove(id)
        save()
        return true
    }


    private fun validateLogin(login: String) {
        if (login.trim().isEmpty()) {
            throw IllegalArgumentException("Логин не может быть пустой строкой")
        }
        if (loginExists(login)) {
            throw IllegalArgumentException("Пользователь с таким логином уже существует")
        }
    }

    private fun save() {
        try {
            File(sourcePath).writeText(Json.encodeToString(_users))
        }
        catch (e: IOException) {
            return
        }
        catch (e: SerializationException) {
            return
        }
    }
}