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
    @OptIn(DelicateCoroutinesApi::class)
    fun showImageFromUrl(imageView: ImageView, url: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                if (url.isNullOrEmpty()) {
                    Log.e("Error", "La URL está vacía")

                    return@launch
                }
                val bitmap = withContext(Dispatchers.IO) {
                    val urlConnection = URL(url).openConnection() as HttpURLConnection
                    urlConnection.doInput = true
                    urlConnection.connect()
                    val inputStream = urlConnection.inputStream
                    return@withContext BitmapFactory.decodeStream(inputStream)
                }
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                } else {
                    Log.e("Error", "No se puede mostrar la imagen")
                }
            } catch (exception: Exception) {
                Log.e(ContentValues.TAG, exception.message.toString(), exception)
            } catch (exception: MalformedURLException) {
                Log.e(ContentValues.TAG, exception.message.toString(), exception)
                Toast.makeText(imageView.context, "Error en la URL de la imagen", Toast.LENGTH_SHORT).show()
            } catch (exception: URISyntaxException) {
                Log.e(ContentValues.TAG, exception.message.toString(), exception)
                Toast.makeText(imageView.context, "Error en la sintaxis de URL de la imagen", Toast.LENGTH_SHORT).show()
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