package es.ilerna.proyectodam.vehiclegest.backend

import android.app.Application
import android.content.Context
import es.ilerna.proyectodam.vehiclegest.R
import java.text.SimpleDateFormat
import java.util.*

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
        private var instance: Vehiclegest? = null
        fun appContext(): Context {
            return instance!!.applicationContext
        }


        //Recupera los formatos custom de fecha almacenados en los xml de cadenas string.xml
        fun customDateFormat(time:Long): String {
           val simpleDateFormat = SimpleDateFormat(
                instance!!.resources
                    .getString(R.string.dateFormat), Locale.getDefault()
            )
            return simpleDateFormat.format(Date(time!!))
        }

        /**
         * Interfaz para implementar como se comportará al hacer click a una ficha
         */
        interface AdapterListener {
            fun onSelected(o:Any)
        }

    }
}
