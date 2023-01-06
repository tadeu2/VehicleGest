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
    protected lateinit var dbFirestoreReference: CollectionReference //Referencia a la colección de la base de datos

    /**
     * Metodo que rellena el formulario con los datos de la entidad
     */
    protected abstract fun bindDataToForm()
    /**
     * Metodo que rellena la entidad con los datos del formulario
     */
    protected abstract fun fillDataFromForm() :Any

    /**
     * Añade el documento a la base de datos
     */
    protected abstract fun addDocumentToDataBase()
    /**
     * Actualiza el documento en la base de datos
     * @param documentSnapshot Documento a actualizar
     * @param any Entidad a actualizar
     */
    protected abstract fun updateDocumentToDatabase(documentSnapshot: DocumentSnapshot, any: Any)//Editar documento en la base de datos

    /**
     *  Borra el documento de la base de datos
     *  @param documentSnapshot: DocumentSnapshot
     */
    protected open fun delDocumentSnapshot(documentSnapshot: DocumentSnapshot) {
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
