package org.example.commands

import org.example.ApplicationCore

interface AppCommand {
    var description: String

    fun execute(argument: ApplicationCore)
}