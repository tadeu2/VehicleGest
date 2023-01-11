package es.ilerna.proyectodam.vehiclegest.helpers

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

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
            try {
                val bitmap = withContext(Dispatchers.IO) {

                    // Establecer la conexión con la URL y descargar la imagen
                    val urlConnection =
                        URL(url).openConnection() as HttpURLConnection //Abrir la conexión
                    urlConnection.doInput = true // Indica que se va a descargar
                    urlConnection.connect() // Conectar con la URL y descargar la imagen
                    val inputStream =
                        urlConnection.inputStream  // Obtener el InputStream de la imagen
                    // Convertir la imagen descargada a un Bitmap
                    return@withContext BitmapFactory.decodeStream(inputStream) // Devolver el Bitmap

                }

                // Mostrar la imagen en el ImageView si no es null (si no hay ningún error)
                if (bitmap != null) {
                    // Pintar la imagen en el ImageView
                    imageView.setImageBitmap(bitmap)
                } else {
                    // Si no se ha podido descargar la imagen, mostrar un mensaje de error
                    Log.e("Error", "No se puede mostrar la imagen")
                }
            } catch (exception: Exception) {
                // Si hay algún error, imprimir un mensaje en la consola
                Log.e(ContentValues.TAG, exception.message.toString(), exception)
            } catch (exception: MalformedURLException) {
                // Si hay algún error, imprimir un mensaje en la consola
                Log.e(ContentValues.TAG, exception.message.toString(), exception)
                Toast.makeText(
                    Vehiclegest.instance.applicationContext,
                    "Error en la URL de la imagen",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (exception: URISyntaxException) {
                // Si hay algún error, imprimir un mensaje en la consola
                Log.e(ContentValues.TAG, exception.message.toString(), exception)
                Toast.makeText(
                    imageView.context,
                    "Error en la sintaxis de URL de la imagen",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    companion object {
        /**
         * Recupera los formatos custom de fecha almacenados en los xml de cadenas string.xml
         * @param time Fecha que querremos darle formato
         */
        fun dateToStringFormat(time: Date?): String {
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
            try {
                fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, fragment)
                    .commit()
            } catch (e: URISyntaxException) {
                Log.e("Error", "Error al cambiar de fragmento")
                throw URISyntaxException("Error al cambiar de fragmento", "Error")
            }
        }


        fun stringToDateFormat(time: String): Date {
           if (time == "") {
               return Date()
           }
            val simpleDateFormat = SimpleDateFormat(
                Vehiclegest.instance.resources
                    .getString(R.string.dateFormat), Locale.getDefault()
            )
            return simpleDateFormat.parse(time)!!
        }


        fun showShortToast(message: String) {
            Toast.makeText(Vehiclegest.instance.applicationContext, message, Toast.LENGTH_SHORT)
                .show()
        }

        fun showLongToast(message: String) {
            Toast.makeText(Vehiclegest.instance.applicationContext, message, Toast.LENGTH_LONG)
                .show()
        }

        fun isInspectionExpired(expiryDateITV: Date, currentDate: Date): Boolean {
            // Obtenemos la diferencia en días entre las dos fechas
            val diff = currentDate.time - expiryDateITV.time
            val diffDays = diff / (24 * 60 * 60 * 1000)

            // Si la diferencia en días es mayor que el número de días permitidos
            // para una inspección, entonces la inspección está caducada
            return diffDays > 365
        }


        fun checkInspectionExpiration(vehicleId: String) :Boolean {
            val firestore = FirebaseFirestore.getInstance()
            val collection = firestore.collection("vehicles")
            val document = collection.document(vehicleId)

            document.get().addOnSuccessListener { snapshot ->
                val expirationDate = snapshot.getDate("expiryDateITV")
                if (expirationDate != null) {
                    val currentDate = Date()
                    if (currentDate.after(expirationDate)) {
                        return@addOnSuccessListener
                    } else {
                        return@addOnSuccessListener
                    }
                }
            }
            return false
        }

       /* fun checkVehicleProblems(){
            val firestore = FirebaseFirestore.getInstance()
            val collection = firestore.collection("vehicles")
            for (vehicle in Vehiclegest.vehiclesList) {
                val document = collection.document(vehicle.id)
                document.get().addOnSuccessListener { snapshot ->
                    val problems = snapshot.get("problems") as ArrayList<String>
                    if (problems.isNotEmpty()) {
                        vehicle.problems = problems
                    }
                }
            }
        }*/
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