package es.ilerna.proyectodam.vehiclegest.backend

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
    private lateinit var navBarBot: BottomNavigationView
    private lateinit var floatingButton: FloatingActionButton
    //Variable que almacenará la referencia a la colección de firestore
    lateinit var db: CollectionReference

    //Método abstracto que implementará cada clase que herede para
    open fun addData() {} //Enlazar datos al formulario de texto

    //Fase de creación de la actividad en el ciclo de vida de la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        //Salva el estado de la actividad
        super.onCreate(savedInstanceState)
        //Inicializa las variables y sconde barras de navegación pasándole las referencias
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarTop.visibility = GONE
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = GONE
        floatingButton = requireActivity().findViewById(R.id.addButton)
        floatingButton.visibility = GONE
    }

    //Fase del ciclo de vida de la actividad cuando esta se destruye
    override fun onDestroy() {
        super.onDestroy()
        //Las barras y el botón flotante vuelven a ser visible al destruirse el fragmento
        navBarTop.visibility = VISIBLE
        navBarBot.visibility = VISIBLE
        floatingButton.visibility = VISIBLE
    }

    //
    fun fragmentReplacer(fragment: Fragment, fragmentManager:FragmentManager) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment)
        fragmentTransaction.commit()
    }

}
