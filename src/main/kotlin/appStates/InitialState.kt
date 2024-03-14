package org.example.states
import org.example.ApplicationCore
import org.example.commandFactories.AppCommandFactory
import org.example.commandFactories.InitialStateNoUserAppCommandFactory
import org.example.commandFactories.InitialStateUserInAppCommandFactory
import org.example.utils.CommandReader

class InitialState(application: ApplicationCore): AppState(application) {
    override fun process() {
        val factory: AppCommandFactory<ApplicationCore> =
            if (application.session.user != null) InitialStateUserInAppCommandFactory()
            else InitialStateNoUserAppCommandFactory()
        val commandReader = CommandReader(factory)

        commandReader.displayCommands()
        commandReader.readAndExecute(application)
    }
}