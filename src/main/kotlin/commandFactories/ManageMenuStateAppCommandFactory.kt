package org.example.commandFactories

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.commands.menu.AddMenuItemAppCommand
import org.example.commands.menu.EditMenuItemAppCommand
import org.example.commands.menu.RemoveMenuItemAppCommand
import org.example.commands.navigation.BackAppCommand

class ManageMenuStateAppCommandFactory: AppCommandFactory<ApplicationCore> {
    override fun createCommandSet(): Iterable<AppCommand> {
        return listOf(AddMenuItemAppCommand(), EditMenuItemAppCommand(), RemoveMenuItemAppCommand(), BackAppCommand())
    }
}