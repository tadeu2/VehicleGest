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
    private lateinit var navBarTop: MaterialToolbar //Barra de navegación superior
    private lateinit var navBarBottom: BottomNavigationView //Barra de navegación inferior
    private lateinit var floatingButton: FloatingActionButton //Botón flotante
    lateinit var dbFirestoreReference: CollectionReference //Referencia a la colección de firestore

    /**
     * Método abstracto que se implementará en cada fragmento para realizar la adición a la base de datos
     */
    abstract fun addDocumentToDatabase() //Añadir datos a la base de datos

    /**
     * Método que se ejecuta al crear la vista del fragmento, se encarga de inicializar las variables
     * de las barras de navegación y el botón flotante.
     */
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

    /**
     * Método que se ejecuta al destruir la vista del fragmento, se encarga de mostrar las barras de
     * navegación y el botón flotante.
     */
    override fun onDestroy() {
        super.onDestroy()
        navBarTop.visibility = VISIBLE
        navBarBottom.visibility = VISIBLE
        floatingButton.visibility = VISIBLE
    }

}
