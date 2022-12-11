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
        var instance: Vehiclegest? = null
        fun appContext(): Context {
            return instance!!.applicationContext
        }
    }
}