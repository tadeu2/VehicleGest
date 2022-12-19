package es.ilerna.proyectodam.vehiclegest.backend

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance

/**
 * Clase principal de la aplicación
 */

class Vehiclegest : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    //Variables y métodos estáticos
    companion object {
        //Devolvemos el contexto de la aplicación con todos sus recursos
        lateinit var instance: Vehiclegest
            private set
    }
}