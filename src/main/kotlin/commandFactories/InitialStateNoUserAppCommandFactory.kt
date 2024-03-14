package org.example.commandFactories

import org.example.ApplicationCore
import org.example.commands.AppCommand
import org.example.commands.auth.ExitAppCommand
import org.example.commands.auth.DeleteAccountAppCommand
import org.example.commands.auth.LoginAppCommand
import org.example.commands.auth.RegisterAppCommand

class InitialStateNoUserAppCommandFactory: AppCommandFactory<ApplicationCore> {
    override fun createCommandSet(): Iterable<AppCommand> {
        return listOf(LoginAppCommand(), RegisterAppCommand(), DeleteAccountAppCommand(), ExitAppCommand())
    }
}
