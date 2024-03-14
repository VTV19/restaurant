package org.example.crud

import org.example.crud.Storage
import org.example.entities.User

interface UserStorage: Storage {
    fun loginExists(login: String): Boolean

    fun createUser(login: String, password: String, isAdmin: Boolean): User

    fun getUser(login: String): User?

    fun getUser(id: Int): User?

    fun removeUser(id: Int): Boolean
}