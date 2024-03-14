package org.example.states

import org.example.ApplicationCore

abstract class AppState(val application: ApplicationCore, val previousState: AppState? = null) {
    abstract fun process()
}