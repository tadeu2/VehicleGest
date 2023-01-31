package es.ilerna.proyectodam.vehiclegest.backend

import android.app.Application

/**
 * Clase principal de la aplicación
 * Se usa para inicializar las variables estáticas
 *
 */

class Vehiclegest : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializar las variables estáticas
        instance = this // Para poder acceder a la aplicación desde cualquier sitio
    }

    //Variables y métodos estáticos
    companion object {
        //Devolvemos el contexto de la aplicación con todos sus recursos
        lateinit var instance: Vehiclegest
            private set //Para que no se pueda modificar desde fuera
    }
}