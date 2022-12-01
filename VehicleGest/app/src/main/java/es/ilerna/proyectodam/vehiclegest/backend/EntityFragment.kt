package es.ilerna.proyectodam.vehiclegest.backend

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.ui.alerts.AlertsFragment
import java.util.Objects

/**
 * Interfaz para crear escuchadores para las diferentes entidades de la base de datos Firestore
 */
abstract class EntityFragment : Fragment() {

    // private lateinit var navBarTop: MaterialToolbar
    lateinit var navBarBot: BottomNavigationView

    fun addEntity(id: String) {}
    fun details(entity:Objects) {}
    fun edit(entity: Objects) {}
    fun delEntity(id: String) {}

    private fun onBtClose(fragment: Fragment) {
        navBarBot.visibility = View.VISIBLE
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment)
        fragmentTransaction.commit()
    }
}
