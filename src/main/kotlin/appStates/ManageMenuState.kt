package org.example.states

import org.example.ApplicationCore
import org.example.commandFactories.ManageMenuStateAppCommandFactory
import org.example.utils.CommandReader
import org.example.utils.OutputConsole

class ManageMenuState(application: ApplicationCore, previousState: AppState?): AppState(application, previousState) {
    override fun process() {
        OutputConsole.displayMenu(application.menuStorage.getMenuItems(), application.session.user!!.isAdmin)

        val commandReader = CommandReader(ManageMenuStateAppCommandFactory())
        println()
        commandReader.displayCommands()
        commandReader.readAndExecute(application)
    }
}