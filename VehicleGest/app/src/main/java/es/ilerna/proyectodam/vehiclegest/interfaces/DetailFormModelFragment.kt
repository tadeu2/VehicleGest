package es.ilerna.proyectodam.vehiclegest.interfaces

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.View.GONE
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.showLongToast
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.showShortToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Interfaz que sirve como base para los fragmentos que muestran los detalles de un modelo
 */
abstract class DetailFormModelFragment : Fragment() {

    //Variables que almacenarán las instancias de los controles de la interfaz
    private lateinit var topToolBar: MaterialToolbar //Barra de navegación superior
    private lateinit var navBarBot: BottomNavigationView //Barra de navegación inferior
    private lateinit var searchTopBar: MaterialToolbar //Barra de búsqueda superior
    private lateinit var floatingAddButton: FloatingActionButton //Botón flotante de la interfaz
    private lateinit var buttonSave: ActionMenuItemView //Botón de guardar
    private lateinit var buttonDelete: ActionMenuItemView //Botón de eliminar
    private lateinit var buttonEdit: ActionMenuItemView//Botón de editar
    private lateinit var buttonClose: ActionMenuItemView //Botón de cancelar
    private lateinit var imageView: View //Imagen de fondo
    lateinit var dbFirestoreReference: CollectionReference
    lateinit var mainFragment: Fragment //Fragmento al que se debe volver
    var documentSnapshot: DocumentSnapshot? = null //Documento que se está mostrando
    private var isAddFragment: Boolean = false //Indica si el fragmento es de añadir o de editar
    val editableEditTextColor: Int = Color.RED //Color de los EditText editables
    //Color de los EditText editables
    /**
     * Fase de creación del fragmento
     * @param savedInstanceState Bundle
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //Inicializa las variables y esconde barras de navegación pasándole las referencias

            initializeUI()

            //Rellenar los datos del formulario con los datos del documento
            if (!isAddFragment) {
                bindDataToForm()
            }
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     *  Añade el documento a la base de datos
     */
    protected open
    fun addDocumentToDataBase() {
        CoroutineScope(Dispatchers.IO).launch {
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


    open fun setCloseButtonListener() {
        topToolBar.setNavigationOnClickListener {
            navigateToMainFragment()
        }
    }

    private fun setDeleteButtonListener() {
        buttonDelete.setOnClickListener {
            delDocumentSnapshot(documentSnapshot)
            navigateToMainFragment()
        }
    }

    private fun setEditButtonListener() {
        buttonEdit.setOnClickListener {
            buttonEdit.visibility = GONE
            buttonSave.visibility = View.VISIBLE
            makeFormEditable()
        }
    }

    private fun setSaveButtonListener() {
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
    protected open
    fun updateDocumentToDatabase(documentSnapshot: DocumentSnapshot, any: Any) {
        CoroutineScope(Dispatchers.IO).launch {
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
    protected open
    fun delDocumentSnapshot(documentSnapshot: DocumentSnapshot?) {
        CoroutineScope(Dispatchers.IO).launch {
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
    }

    private fun initializeUI() {
        //Inicializa las variables y sconde barras de navegación pasándole las referencias
        topToolBar = requireActivity().findViewById(R.id.topToolbar)
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        searchTopBar = requireActivity().findViewById(R.id.searchToolbar)
        floatingAddButton = requireActivity().findViewById(R.id.addButton)
        buttonDelete = requireActivity().findViewById(R.id.deleteButton)
        buttonSave = requireActivity().findViewById(R.id.saveButton)
        buttonEdit = requireActivity().findViewById(R.id.editButton)


        searchTopBar.visibility = GONE
        topToolBar.visibility = GONE
        navBarBot.visibility = GONE
        floatingAddButton.visibility = GONE

        if (isAddFragment) {
            makeFormEditable()
            buttonDelete.visibility = GONE
            buttonEdit.visibility = GONE
            buttonSave.visibility = View.VISIBLE
        }

        setCloseButtonListener()
        setEditButtonListener()
        setSaveButtonListener()
        setDeleteButtonListener()
    }

    /**
     * Hace editable el formulario
     */
    abstract fun makeFormEditable()

    /**
     * Rellena los datos del formulario con los datos del documento
     */
    abstract fun fillDataFromForm(): Any

    /**
     *  Crea un documento con los datos del formulario
     */
    abstract fun bindDataToForm()
}

