package org.example.states

import org.example.ApplicationCore
import org.example.commandFactories.AppCommandFactory
import org.example.commandFactories.OrdersStateAdminAppCommandFactory
import org.example.commandFactories.OrdersStateVisitorAppCommandFactory
import org.example.utils.CommandReader

class OrdersState(application: ApplicationCore, previousState: AppState?): AppState(application, previousState) {
    override fun process() {
        val isAdmin = application.session.user!!.isAdmin

        val factory: AppCommandFactory<ApplicationCore> =
            if (isAdmin) OrdersStateAdminAppCommandFactory()
            else OrdersStateVisitorAppCommandFactory()


        val reader = CommandReader(factory)
        reader.displayCommands()
        reader.readAndExecute(application)
    }
}