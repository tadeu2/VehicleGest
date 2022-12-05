package es.ilerna.proyectodam.vehiclegest.backend

import androidx.fragment.app.Fragment

/**
 * Interfaz para crear escuchadores para las diferentes entidades de la base de datos Firestore
 */
abstract class ModelFragment() : Fragment() {

    abstract fun onAddButtonClick()
}
