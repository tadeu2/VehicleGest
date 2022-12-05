package es.ilerna.proyectodam.vehiclegest.backend

import android.app.Application
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.imageview.ShapeableImageView
import es.ilerna.proyectodam.vehiclegest.R
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
         * Ejecuta un hilo paralelo para asignar una imagen URL al campo de imagen
         */
        fun displayImgURL(url: String?, imgView: ShapeableImageView?) {

            try {
                //Crea un hilo paralelo para descargar las imagenes de una URL
                val executor = Executors.newSingleThreadExecutor()
                executor.execute {
                    val u = URL(url)
                    //Declaramos un manejador que asigne la imagen al objecto imagen
                    val handler = Handler(Looper.getMainLooper())

                    //Creamos el objecto imagen vacio y le asignamos por stream a otra variable
                    val im = u.openStream()
                    val image = BitmapFactory.decodeStream(im)

                    //Para hacer cambios en la interfaz
                    handler.post {
                        imgView?.setImageBitmap(image)
                    }
                }
            } catch (e: Exception) {
                //TODO Lanzar mensaje de error en popup
                e.printStackTrace()
            }
        }

        fun fragmentReplacer(fragment: Fragment, parentFragmentManager: FragmentManager) {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment)
            fragmentTransaction.commit()
        }
    }
}
