package org.example.entities

class Event<T> {
    private val _observers: MutableList<(T) -> Unit> = mutableListOf()

    operator fun invoke(argument: T) {
        _observers.forEach {it.invoke(argument)}
    }

    operator fun plusAssign(observerMethod: (T) -> Unit) {
        _observers.add(observerMethod)
    }

    operator fun minusAssign(observerMethod: (T) -> Unit) {
        _observers.remove(observerMethod)
    }
}