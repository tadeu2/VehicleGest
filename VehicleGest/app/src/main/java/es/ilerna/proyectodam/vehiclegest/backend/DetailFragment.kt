package es.ilerna.proyectodam.vehiclegest.backend

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle

/**
 * Interfaz para crear escuchadores para las diferentes entidades de la base de datos Firestore
 */
abstract class DetailFragment : Fragment() {

    lateinit var navBarTop: MaterialToolbar
    lateinit var navBarBot: BottomNavigationView

    abstract fun bindData() //Enlazar datos al formulario de texto
    abstract fun editDocument(data: Vehicle)
    abstract fun delDocument(data: Vehicle)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Esconde barras de navegación
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarTop.visibility = GONE
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        //La barra superior vuelve a ser visible al destruirse el fragmento
        navBarTop.visibility = View.VISIBLE
    }

    open fun onBtClose(fragment: Fragment) {
        navBarBot.visibility = View.VISIBLE
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment)
        fragmentTransaction.commit()
    }
}
