package org.example.states

import org.example.ApplicationCore
import org.example.commandFactories.AppCommandFactory
import org.example.commandFactories.MainMenuStateAdminAppCommandFactory
import org.example.commandFactories.MainMenuStateVisitorAppCommandFactory
import org.example.utils.CommandReader

class MainMenuState(application: ApplicationCore, previousState: AppState?): AppState(application, previousState) {
    override fun process() {

        val factory: AppCommandFactory<ApplicationCore> =
            if (application.session.user!!.isAdmin) MainMenuStateAdminAppCommandFactory()
            else MainMenuStateVisitorAppCommandFactory()
        val commandReader = CommandReader(factory)

        commandReader.displayCommands()
        commandReader.readAndExecute(application)
    }
}