package es.ilerna.proyectodam.vehiclegest.backend

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.imageview.ShapeableImageView
import es.ilerna.proyectodam.vehiclegest.R
import kotlinx.coroutines.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

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

        /**
         * Recupera los formatos custom de fecha almacenados en los xml de cadenas string.xml
         */
        fun customDateFormat(time: Date): String {
            val simpleDateFormat = SimpleDateFormat(
                instance!!.resources
                    .getString(R.string.dateFormat), Locale.getDefault()
            )
            return simpleDateFormat.format(time)
        }

        fun customReverseDateFormat(time: String): Date {
            val simpleDateFormat = SimpleDateFormat(
                instance!!.resources
                    .getString(R.string.dateFormat), Locale.getDefault()
            )
            return simpleDateFormat.parse(time) as Date
        }

        /**
         * Interfaz para implementar como se comportará al hacer click a una ficha
         */
        interface AdapterListener {
            fun onSelected(o: Any)
        }

        /**
         * Función para cambiar de fragment
         */
        fun fragmentReplacer(fragment: Fragment, fm: FragmentManager) {
            fm.beginTransaction().replace(R.id.nav_host_fragment_content_main, fragment).commit()
        }

    }
}