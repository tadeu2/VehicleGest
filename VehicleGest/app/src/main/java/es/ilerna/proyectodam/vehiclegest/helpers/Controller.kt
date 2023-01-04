package es.ilerna.proyectodam.vehiclegest.helpers

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 * Clase que contiene funciones que se usan en varios sitios
 */
class Controller {
    /**
     * Función que descarga una imagen de una URL y la pinta en un ImageView
     * @param url Parámetro que contiene la URL de la imagen
     * @param imageView Parámetro que contiene el ImageView donde se pintará la imagen
     */
    @OptIn(DelicateCoroutinesApi::class) //Para evitar el warning de que la función es experimental
    fun showImageFromUrl(imageView: ImageView, url: String) {
        // Crear una coroutine con el contexto de la UI
        // Se usa el contexto de la UI porque se va a pintar en un ImageView
        GlobalScope.launch(Dispatchers.Main) {
            // Descargar la imagen en una coroutine suspendida
            val bitmap = withContext(Dispatchers.IO) {
                try {
                    // Establecer la conexión con la URL y descargar la imagen
                    val urlConnection =
                        URL(url).openConnection() as HttpURLConnection //Abrir la conexión
                    urlConnection.doInput = true // Indica que se va a descargar
                    urlConnection.connect() // Conectar con la URL y descargar la imagen
                    val inputStream =
                        urlConnection.inputStream  // Obtener el InputStream de la imagen
                    // Convertir la imagen descargada a un Bitmap
                    return@withContext BitmapFactory.decodeStream(inputStream) // Devolver el Bitmap
                } catch (exception: Exception) {
                    // Si hay algún error, imprimir un mensaje en la consola
                    Log.e("Error", "Error al descargar y mostrar la imagen", exception)

                }
                // Si hay algún error, devolver null para que no se pinte nada
                return@withContext null
            }

            // Mostrar la imagen en el ImageView si no es null (si no hay ningún error)
            if (bitmap != null) {
                // Pintar la imagen en el ImageView
                imageView.setImageBitmap(bitmap)
            } else {
                // Si no se ha podido descargar la imagen, mostrar un mensaje de error
                Log.e("Error", "Error al descargar y mostrar la imagen")
            }

        }
    }

    /**
     * Ejecuta un hilo paralelo para asignar una imagen URL al campo de imagen
     */
    fun displayImgURL(url: String?, imgView: ShapeableImageView?) {

        try {
            //Crea un hilo paralelo para descargar las imagenes de una URL
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {

                if (isUrlValid(url)) {

                    //Declaramos un manejador que asigne la imagen al objecto imagen
                    val handler = Handler(Looper.getMainLooper())

                    //Creamos el objecto imagen vacio y le asignamos por stream a otra variable
                    val im = URL(url).openStream()
                    val image = BitmapFactory.decodeStream(im)

                    //Para hacer cambios en la interfaz
                    handler.post {
                        imgView?.setImageBitmap(image)
                    }
                } else {
                    Log.d(ContentValues.TAG, "BAD URL FORMAT")
                }

            }
        } catch (exception: Exception) {
            throw exception
        }
    }


    /**
     * Prueba si la URL en texto es válida o no
     */
    private fun isUrlValid(url: String?): Boolean {
        return try {
            URL(url).toURI()
            true
        } catch (e: MalformedURLException) {
            false
        } catch (e: URISyntaxException) {
            false
        }
    }

    companion object {
        /**
         * Recupera los formatos custom de fecha almacenados en los xml de cadenas string.xml
         * @param time Fecha que querremos darle formato
         */
        fun customDateFormat(time: Date): String {
            val simpleDateFormat = SimpleDateFormat(
                Vehiclegest.instance.resources
                    .getString(R.string.dateFormat), Locale.getDefault()
            )
            return simpleDateFormat.format(time)
        }

        /**
         * Función para cambiar de fragment
         * @param fragment Fragmento que se carga
         * @param fragmentManager Manejador de fragmentos
         */
        fun fragmentReplacer(fragment: Fragment, fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, fragment).commit()
        }


        fun customReverseDateFormat(time: String): Date {
            val simpleDateFormat = SimpleDateFormat(
                Vehiclegest.instance.resources
                    .getString(R.string.dateFormat), Locale.getDefault()
            )
            return simpleDateFormat.parse(time) as Date
        }

    }

    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface AdapterListener {
        /**
         * Función que determina que hacer al hacer click a una ficha
         * @param documentSnapshot Parámetro que contiene la instancia
         */
        fun onItemSelected(documentSnapshot: DocumentSnapshot?)

        /**
         * Función que determina que hacer al hacer click al botón de añadir
         */
        fun onAddButtonClick()
    }

}