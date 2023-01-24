package es.ilerna.proyectodam.vehiclegest.interfaces

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.showLongToast
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.showShortToast
import java.util.concurrent.Executors


/**
 * Interfaz que sirve como base para los fragmentos que muestran los detalles de un modelo
 */
abstract class DetailModelFragment : Fragment() {

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
    protected abstract fun fillDataFromForm(): Any

    /**
     *  Hace el formulario editable
     */
    protected abstract fun makeFormEditable()

    /**
     *  Añade el documento a la base de datos
     */
    protected open fun addDocumentToDataBase() {
        Executors.newSingleThreadExecutor().execute {
            dbFirestoreReference.add(fillDataFromForm())
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot escrito con ID: ${documentReference.id}"
                    )
                    showLongToast("Documento añadido correctamente")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error añadiendo documento", e)
                    showLongToast("Error añadiendo documento")
                }
        }
    }

    /**
     * Metodo para inicializar los escuchadores de la interfaz
     * @param documentSnapshot Instantanea de firestore de la entidad
     * @param fragmentManager Administrador de fragmentos
     * @param fragment Fragmento al que se debe volver
     */
    protected open fun setListeners(
        documentSnapshot: DocumentSnapshot?,
        parentFragmentManager: FragmentManager,
        fragment: Any,
        buttonClose: Button,
        buttonDelete: Button,
        buttonSave: Button,
        buttonEdit: Button
    ) {
        //Escuchador del boton cerrar
        buttonClose.setOnClickListener {
            fragmentReplacer(fragment as Fragment, parentFragmentManager)
        }

        if (documentSnapshot != null) {
            if (documentSnapshot.exists()) {
                //Escuchador del boton eliminar
                 buttonDelete.setOnClickListener {
                    delDocumentSnapshot(documentSnapshot)
                    fragmentReplacer(fragment as Fragment, parentFragmentManager)
                }

                //Escuchador del boton editar
                buttonSave.setOnClickListener {
                    updateDocumentToDatabase(documentSnapshot, fillDataFromForm())
                    fragmentReplacer(fragment as Fragment, parentFragmentManager)
                }
            } else {
                showShortToast("El documento no existe")
                fragmentReplacer(fragment as Fragment, parentFragmentManager)
            }
        } else {
            //Escuchador del boton editar
            buttonSave.setOnClickListener {
                addDocumentToDataBase()
                fragmentReplacer(fragment as Fragment, parentFragmentManager)
            }
        }

        buttonEdit.setOnClickListener {
            //Escuchador del botón de editar
            makeFormEditable()
            buttonSave.visibility = VISIBLE
            buttonEdit.visibility = GONE
        }
    }

    /**
     * Actualiza el documento en la base de datos
     * @param documentSnapshot Documento a actualizar
     * @param any Entidad a actualizar
     */
    protected open fun updateDocumentToDatabase(documentSnapshot: DocumentSnapshot, any: Any) {
        Executors.newSingleThreadExecutor().execute {
            val documentReference = dbFirestoreReference.document(documentSnapshot.id)
            documentReference.set(any) //Actualiza el documento con los datos del formulario
                .addOnSuccessListener {
                    Log.d("Vehiclegest", "Document successfully updated!")
                    showShortToast("Documento actualizado correctamente")
                }
                .addOnFailureListener { e ->
                    Log.w("vehiclegest", "Error updating document", e)
                    showShortToast("Error actualizando documento")
                }
        }
    }

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
                    showShortToast("Documento borrado correctamente")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error borrando documento", e)
                    showShortToast("Error borrando documento")
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
        val toolbar = R.id.topDetailToolbar

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
