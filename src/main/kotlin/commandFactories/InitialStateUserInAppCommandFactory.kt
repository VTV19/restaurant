package org.example.commandFactories

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.commands.auth.DeleteAccountAppCommand
import org.example.commands.auth.ExitAppCommand
import org.example.commands.auth.LogoutAppCommand
import org.example.commands.navigation.MainMenuAppCommand

class InitialStateUserInAppCommandFactory: AppCommandFactory<ApplicationCore> {
    override fun createCommandSet(): Iterable<AppCommand> {
        return listOf(MainMenuAppCommand(), LogoutAppCommand(), DeleteAccountAppCommand(), ExitAppCommand())
    }
}