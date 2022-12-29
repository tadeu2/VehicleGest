package es.ilerna.proyectodam.vehiclegest.helpers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import java.text.SimpleDateFormat
import java.util.*

/**
 * Clase con métodos estáticos de apoyo
 */
class DataHelper {

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
        fun onItemSelected(snapshot: DocumentSnapshot?)
        //Función que se encarga de añadir un registro de alerta
        fun onAddButtonClick()
    }

}