package es.ilerna.proyectodam.vehiclegest.interfaces

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.showLongToast
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.showShortToast
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity
import java.util.concurrent.Executors


/**
 * Interfaz que sirve como base para los fragmentos que muestran los detalles de un modelo
 */
abstract class DetailFormModelFragment: Fragment() {

    //Variables que almacenarán las instancias de las barras de navegación y el bóton flotante
    private lateinit var navBarTop: MaterialToolbar //Barra de navegación superior
    private lateinit var navBarBot: BottomNavigationView //Barra de navegación inferior
    private lateinit var floatingButton: FloatingActionButton //Botón flotante de la interfaz
    private lateinit var buttonSave: MaterialButton //Botón de guardar
    private lateinit var buttonDelete: MaterialButton //Botón de eliminar
    private lateinit var buttonEdit: MaterialButton //Botón de editar
    private lateinit var imageView: View //Imagen de fondo
    lateinit var dbFirestoreReference: CollectionReference
    lateinit var mainFragment: Fragment //Fragmento al que se debe volver
    var documentSnapshot: DocumentSnapshot? = null //Documento que se está mostrando
    private var isAddFragment: Boolean = false //Indica si el fragmento es de añadir o de editar

    /**
     * Fase de creación del fragmento
     * @param savedInstanceState Bundle
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //Inicializa las variables y esconde barras de navegación pasándole las referencias
            initializeUI()
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
    }

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

    fun setAddFragment() {
        isAddFragment = !isAddFragment
    }

    private fun navigateToMainFragment() {
        fragmentReplacer(mainFragment, parentFragmentManager)
    }


    open fun setCloseButtonListener(buttonClose: MaterialButton) {
        buttonClose.setOnClickListener {
            navigateToMainFragment()
        }
    }

    fun setDeleteButtonListener(buttonDelete: MaterialButton) {
        buttonDelete.setOnClickListener {
            delDocumentSnapshot(documentSnapshot)
            navigateToMainFragment()
        }
    }

    fun setEditButtonListener(buttonEdit: MaterialButton) {
        buttonEdit.setOnClickListener {
            makeFormEditable()
        }
    }

    fun setSaveButtonListener(buttonSave: MaterialButton) {
        buttonSave.setOnClickListener {
            if (documentSnapshot != null) {
                updateDocumentToDatabase(documentSnapshot!!, fillDataFromForm())
            } else {
                addDocumentToDataBase()
            }
            navigateToMainFragment()
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
    protected open fun delDocumentSnapshot(documentSnapshot: DocumentSnapshot?) {
        try {
            documentSnapshot?.reference?.delete()
                ?.addOnSuccessListener {
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot borrado con ID: ${documentSnapshot.id}"
                    )
                    showShortToast("Documento borrado correctamente")
                }
                ?.addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error borrando documento", e)
                    showShortToast("Error borrando documento")
                }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

    }

    private fun initializeUI() {
        //Inicializa las variables y sconde barras de navegación pasándole las referencias
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        floatingButton = requireActivity().findViewById(R.id.addButton)
        buttonSave = requireActivity().findViewById(R.id.btdelete)
        buttonSave = requireActivity().findViewById(R.id.btsave)
        buttonEdit = requireActivity().findViewById(R.id.btedit)

        navBarTop.visibility = GONE
        navBarBot.visibility = GONE
        floatingButton.visibility = GONE

        if (isAddFragment == true){
            buttonDelete.visibility = GONE
            buttonEdit.visibility = GONE
            buttonSave.visibility = View.VISIBLE
        }
    }

    abstract fun makeFormEditable()
    abstract fun fillDataFromForm(): Any
    abstract fun bindDataToForm()
}

