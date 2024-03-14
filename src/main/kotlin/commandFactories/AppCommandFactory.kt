package org.example.commandFactories

import org.example.commands.AppCommand

interface AppCommandFactory<T> {
    fun createCommandSet(): Iterable<AppCommand>
}