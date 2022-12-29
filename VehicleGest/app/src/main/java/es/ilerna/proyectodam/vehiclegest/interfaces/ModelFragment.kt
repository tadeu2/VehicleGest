package es.ilerna.proyectodam.vehiclegest.interfaces

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.adapters.ITVRecyclerAdapter

/**
 * Interfaz para crear escuchadores para las diferentes entidades de la base de datos Firestore
 */
abstract class ModelFragment() : Fragment() {
    /**
     * Función que se ejecuta al hacer click en el botón añadir
     */
    abstract fun onAddButtonClick()

    /**
     * Función que se ejecuta al hacer click en un elemento del recyclerview
     */
    abstract fun onItemSelected(snapshot: DocumentSnapshot?)

}
