package es.ilerna.proyectodam.vehiclegest.interfaces

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


/**
 * Interfaz para crear escuchadores para las diferentes entidades de la base de datos Firestore
 */
abstract class DetailFragment : Fragment() {

    //Variables que almacenarán las instancias de las barras de navegación y el bóton flotante
    private lateinit var navBarTop: MaterialToolbar //Barra de navegación superior
    private lateinit var navBarBot: BottomNavigationView //Barra de navegación inferior
    private lateinit var floatingButton: FloatingActionButton //Botón flotante de la interfaz
    lateinit var dbFirestoreReference: CollectionReference //Referencia a la colección de la base de datos

    open fun bindDataToForm() {}//Enlazar datos al formulario de texto
    open fun addDataToDataBase() {} //Añadir datos a la base de datos
    open fun updateDataBase() {} //Actualizar datos en la base de datos
    open fun editDocumentSnapshot(documentSnapshot: DocumentSnapshot) {}//Editar documento en la base de datos

    /**
     *  Borra el documento de la base de datos
     *  @param documentSnapshot: DocumentSnapshot
     */
    fun delDocumentSnapshot(documentSnapshot: DocumentSnapshot) {
        try {
            documentSnapshot.reference.delete()
                .addOnSuccessListener {
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot borrado con ID: ${documentSnapshot.id}"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error borrando documento", e)
                }

        } catch (exception: Exception) {
            exception.printStackTrace()
        }

    }

    /**
     * Fase de creación de la actividad en el ciclo de vida de la actividad.
     * @param savedInstanceState: Bundle? (Estado guardado de la actividad)
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
     * Se destrye el fragmento y se vuelve a mostrar las barras de navegación
     */
    override fun onDestroy() {
        super.onDestroy()
        //La barra superior vuelve a ser visible al destruirse el fragmento
        navBarTop.visibility = VISIBLE
        navBarBot.visibility = VISIBLE
        floatingButton.visibility = VISIBLE
    }

}
