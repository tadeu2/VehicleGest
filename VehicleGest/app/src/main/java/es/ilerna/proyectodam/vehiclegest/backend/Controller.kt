package es.ilerna.proyectodam.vehiclegest.backend

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.util.concurrent.Executors

class Controller {
    // Función para descargar e imagen de una URL y mostrarla en un ImageView

    @OptIn(DelicateCoroutinesApi::class)
    fun showImageFromUrl(imageView: ImageView, url: String, progressBar: ProgressBar) {
        // Mostrar la barra de carga
        // Mostrar la barra de progreso
        progressBar.visibility = View.VISIBLE
        // Crear una coroutine con el contexto de la UI
        GlobalScope.launch(Dispatchers.Main) {
            // Descargar la imagen en una coroutine suspendida
            val bitmap = withContext(Dispatchers.IO) {
                try {
                    // Establecer la conexión con la URL y descargar la imagen
                    val urlConnection = URL(url).openConnection() as HttpURLConnection
                    urlConnection.doInput = true
                    urlConnection.connect()
                    val inputStream = urlConnection.inputStream
                    // Convertir la imagen descargada a un Bitmap
                    return@withContext BitmapFactory.decodeStream(inputStream)
                } catch (e: Exception) {
                    // Si hay algún error, imprimir un mensaje en la consola
                    Log.e("Error", "Error al descargar y mostrar la imagen", e)
                    progressBar.visibility = View.GONE
                }
                return@withContext null
            }

            // Mostrar la imagen en el ImageView
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
                // Ocultar la barra de carga
                progressBar.visibility = View.GONE
            } else {
                // Si no se ha podido descargar la imagen, mostrar un mensaje de error
                Log.e("Error", "Error al descargar y mostrar la imagen")
                progressBar.visibility = View.GONE
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
                    val url = URL(url);

                    //Declaramos un manejador que asigne la imagen al objecto imagen
                    val handler = Handler(Looper.getMainLooper())

                    //Creamos el objecto imagen vacio y le asignamos por stream a otra variable
                    val im = url.openStream()
                    val image = BitmapFactory.decodeStream(im)

                    //Para hacer cambios en la interfaz
                    handler.post {
                        imgView?.setImageBitmap(image)
                    }
                } else {
                    Log.d(ContentValues.TAG, "BAD URL FORMAT")
                }

            }
        } catch (e: Exception) {
            throw e
        }
    }


    /**
     * Prueba si la URL en texto es válida o no
     */
    fun isUrlValid(url: String?): Boolean {
        return try {
            URL(url).toURI()
            true
        } catch (e: MalformedURLException) {
            false
        } catch (e: URISyntaxException) {
            false
        }
    }

}