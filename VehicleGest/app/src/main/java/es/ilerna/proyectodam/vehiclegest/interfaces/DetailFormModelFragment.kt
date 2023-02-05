package es.ilerna.proyectodam.vehiclegest.interfaces

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE

import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.showLongToast
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.showShortToast
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Interfaz que sirve como base para los fragmentos que muestran los detalles de un modelo
 */
abstract class DetailFormModelFragment : Fragment(), MenuProvider {

    private lateinit var mainBinding: MainActivity

    //Variables que almacenarán las instancias de los controles de la interfaz
    private lateinit var detailToolBar: Toolbar //Barra de navegación superior
    private lateinit var buttonSave: ActionMenuItemView //Botón de guardar
    private lateinit var buttonDelete: ActionMenuItemView //Botón de eliminar
    private lateinit var buttonEdit: ActionMenuItemView//Botón de editar
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

            mainBinding = activity as MainActivity

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

    private fun initializeUI() {
        //Inicializa las variables y sconde barras de navegación pasándole las referencias

        buttonSave = mainBinding.findViewById(R.id.saveButton)
        buttonEdit = mainBinding.findViewById(R.id.editButton)
        buttonDelete = mainBinding.findViewById(R.id.deleteButton)
        detailToolBar = mainBinding.findViewById(R.id.topDetailToolbar)

        mainBinding.activityMainBinding.apply {
            arrayOf(
                searchView,
                topBarMain.topToolbar,
                bottomBarMain.bottomNavMenu,
                contentMain.addButton,

            ).forEach {
                it.visibility = GONE
            }

            /*  topBarMain.topToolbar.apply {
                  visibility = View.VISIBLE
                  title = getString(R.string.title_detail)
                  setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                  setNavigationOnClickListener {
                      fragmentReplacer(mainFragment, requireActivity().supportFragmentManager)
                  }*/
        }

        val buttons = arrayOf(buttonSave, buttonDelete, buttonEdit)
        if (isAddFragment) {
            makeFormEditable()
            buttons.forEach {
                if (it.id != R.id.saveButton) {
                    it.visibility = GONE
                } else {
                    it.visibility = View.VISIBLE
                }
            }
        } else {
            buttons.forEach {
                if (it.id != R.id.saveButton) {
                    it.visibility = View.VISIBLE
                } else {
                    it.visibility = GONE
                }
            }
        }

        setCloseButtonListener()
        setEditButtonListener()
        setSaveButtonListener()
        setDeleteButtonListener()
    }

    /**
     * Al crear el menú de la barra de navegación superior
     */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // Add menu items here
        menuInflater.inflate(R.menu.detail_bar_items, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.editButton -> {
                buttonEdit.visibility = GONE
                buttonSave.visibility = View.VISIBLE
                makeFormEditable()
                return true
            }
            R.id.saveButton -> {
                if (documentSnapshot != null) {
                    updateDocumentToDatabase(documentSnapshot!!, fillDataFromForm())
                } else {
                    addDocumentToDataBase()
                }
                navigateToMainFragment()
                return true
            }
            R.id.deleteButton -> {
                delDocumentSnapshot(documentSnapshot)
                navigateToMainFragment()
                return true
            }
        }
        return false
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
        detailToolBar.setNavigationOnClickListener {
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

