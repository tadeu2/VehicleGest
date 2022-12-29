package es.ilerna.proyectodam.vehiclegest.interfaces

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import es.ilerna.proyectodam.vehiclegest.R

/**
 * Clase abstracta para los fragmentos de adición a la base de datos, agrupa propiedades y
 * métodos comunes.
 */
abstract class AddFragment() : Fragment() {

    //Variables que almacenarán las instancias de las barras de navegación y el bóton flotante
    private lateinit var navBarTop: MaterialToolbar
    private lateinit var navBarBottom: BottomNavigationView
    private lateinit var floatingButton: FloatingActionButton
    //Variable que almacenará la referencia a la colección de firestore
    lateinit var dbFirestoreReference: CollectionReference

    open fun addDocumentToDatabase() {} //Añadir datos a la base de datos

    //Fase de creación de la actividad en el ciclo de vida de la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        //Salva el estado de la actividad
        super.onCreate(savedInstanceState)
        //Inicializa las variables y sconde barras de navegación pasándole las referencias
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarTop.visibility = GONE
        navBarBottom = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBottom.visibility = GONE
        floatingButton = requireActivity().findViewById(R.id.addButton)
        floatingButton.visibility = GONE
    }

    //Fase del ciclo de vida de la actividad cuando esta se destruye
    override fun onDestroy() {
        super.onDestroy()
        //Las barras y el botón flotante vuelven a ser visible al destruirse el fragmento
        navBarTop.visibility = VISIBLE
        navBarBottom.visibility = VISIBLE
        floatingButton.visibility = VISIBLE
    }

}
