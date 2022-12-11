package es.ilerna.proyectodam.vehiclegest.backend

import android.app.Application
import android.content.Context

/**
 * Clase principal de la aplicación
 */

class Vehiclegest : Application() {

    //Constructor principal de la aplicación vehiclegest
    init {
        //Inicializa la variable instance pasandole la referencia a la
        //instancia de la aplicación principal
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