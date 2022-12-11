package es.ilerna.proyectodam.vehiclegest.backend

import android.app.Application
import android.content.Context

/**
 * Clase principal de la aplicación
 */

class Vehiclegest : Application() {

    //Constructor principal de la aplicación vehiclegest
    init {
        instance = this
    }

    //Variables y métodos estáticos
    companion object {

        //Devolvemos el contexto de la aplicación con todos sus recursos
        var instance: Vehiclegest? = null
        fun appContext(): Context {
            return instance!!.applicationContext
        }
    }
}