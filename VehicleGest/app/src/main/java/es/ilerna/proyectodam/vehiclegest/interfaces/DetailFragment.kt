package es.ilerna.proyectodam.vehiclegest.interfaces

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import io.grpc.InternalChannelz.id
import org.checkerframework.checker.units.qual.s
import java.util.concurrent.Executors


/**
 * Interfaz para crear escuchadores para las diferentes entidades de la base de datos Firestore
 */
abstract class DetailFragment() : Fragment() {

    //Variables que almacenarán las instancias de las barras de navegación y el bóton flotante
    private lateinit var navBarTop: MaterialToolbar
    private lateinit var navBarBot: BottomNavigationView
    private lateinit var floatingButton: FloatingActionButton
    //Variable que almacenará la referencia a la colección de firestore
    lateinit var dbFirestoreReference: CollectionReference

    open fun bindDataToForm() {} //Enlazar datos al formulario de texto
    open fun addData() {} //Añadir datos a la base de datos
    open fun updateData() {} //Actualizar datos en la base de datos
    open fun editDocument(snapshot: DocumentSnapshot) {}//Editar documento en la base de datos

    /**
     *  Borra el documento de la base de datos
     *  @param s: DocumentSnapshot
     */
    fun delDocument(snapshot: DocumentSnapshot) {
            try {
                snapshot.reference.delete()
                    .addOnSuccessListener {
                        Log.d(
                            ContentValues.TAG,
                            "DocumentSnapshot borrado con ID: ${snapshot.id}"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error borrando documento", e)
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }

    }

    /**
     * Fase de creación de la actividad en el ciclo de vida de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inicializa las variables y sconde barras de navegación pasándole las referencias
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarTop.visibility = GONE
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = GONE
        floatingButton = requireActivity().findViewById(R.id.addButton)
        floatingButton.visibility = GONE

    }

    /**
     * Fase del ciclo de vida de la actividad cuando esta se destruye
     */
    override fun onDestroy() {
        super.onDestroy()
        //La barra superior vuelve a ser visible al destruirse el fragmento
        navBarTop.visibility = VISIBLE
        navBarBot.visibility = VISIBLE
        floatingButton.visibility = VISIBLE
    }

}
