package es.ilerna.proyectodam.vehiclegest.backend

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.R
import java.util.concurrent.Executors

/**
 * Interfaz para crear escuchadores para las diferentes entidades de la base de datos Firestore
 */
abstract class DetailFragment(open val s: DocumentSnapshot) : Fragment() {

    lateinit var navBarTop: MaterialToolbar
    lateinit var navBarBot: BottomNavigationView
    lateinit var floatingButton: FloatingActionButton
    lateinit var db: CollectionReference

    open fun bindData() {} //Enlazar datos al formulario de texto
    open fun editDocument(s: DocumentSnapshot) {}

    open fun delDocument(s: DocumentSnapshot) {

        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                db.document(s.id).delete()
                    .addOnSuccessListener {
                        Log.d(
                            ContentValues.TAG,
                            "DocumentSnapshot borrado con ID: ${s.id}"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error borrando documento", e)
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Esconde barras de navegación
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarTop.visibility = GONE
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = GONE
        floatingButton = requireActivity().findViewById(R.id.addButton)
        floatingButton.visibility = GONE

    }

    override fun onDestroy() {
        super.onDestroy()
        //La barra superior vuelve a ser visible al destruirse el fragmento
        navBarTop.visibility = VISIBLE
        navBarBot.visibility = VISIBLE
        floatingButton.visibility = VISIBLE
    }

    fun fragmentReplacer(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment)
        fragmentTransaction.commit()
    }

}
