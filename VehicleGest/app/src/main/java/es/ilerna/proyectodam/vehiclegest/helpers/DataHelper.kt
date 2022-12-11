package es.ilerna.proyectodam.vehiclegest.helpers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import java.text.SimpleDateFormat
import java.util.*

/**
 * Clase con métodos estáticos de apoyo
 */
class DataHelper {

    /**
     * Recupera los formatos custom de fecha almacenados en los xml de cadenas string.xml
     */
    fun customDateFormat(time: Date): String {
        val simpleDateFormat = SimpleDateFormat(
            Vehiclegest.instance!!.resources
                .getString(R.string.dateFormat), Locale.getDefault()
        )
        return simpleDateFormat.format(time)
    }

    fun customReverseDateFormat(time: String): Date {
        val simpleDateFormat = SimpleDateFormat(
            Vehiclegest.instance!!.resources
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