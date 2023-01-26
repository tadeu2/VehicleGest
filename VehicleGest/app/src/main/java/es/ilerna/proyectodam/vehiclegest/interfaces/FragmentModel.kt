package es.ilerna.proyectodam.vehiclegest.interfaces

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.adapters.FirestoreAdapter
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity

/**
 * Fragmento de listado de empleados
 */
abstract class FragmentModel : Fragment(), RecyclerAdapterListener {

    //Creamos una variable para el adaptador
    lateinit var recyclerAdapter: RecyclerView.Adapter<*>

    private lateinit var recyclerView: RecyclerView

    //Variables que almacenarán las instancias de las barras de navegación y el bóton flotante
    private lateinit var navBarTop: MaterialToolbar //Barra de navegación superior
    private lateinit var navBarBot: BottomNavigationView //Barra de navegación inferior
    private lateinit var floatingButton: FloatingActionButton //Botón flotante de la interfaz
    lateinit var dbFirestoreReference: CollectionReference

    //Referencia a la vista de busqueda
    private lateinit var searchView: SearchView

    //Fase de creación de la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //Inicializa las variables y esconde barras de navegación pasándole las referencias
            initializeUI()
            //Con with(this) nos referimos a la clase que implementa el fragmento
            with(this) {
                //Enlaza la variable searchView a la vista de busqueda
                searchView = (activity as MainActivity).findViewById(R.id.searchView)

                setSearchViewListeners()
                getAllDataFromDatabase()
            }
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
    }

    /**
     * Inicializa las variables y configura las barras de navegación y el botón flotante
     */
    private fun initializeUI() {
        //Inicializa las variables y sconde barras de navegación pasándole las referencias
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarTop.visibility = View.VISIBLE
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = View.VISIBLE
        floatingButton = requireActivity().findViewById(R.id.addButton)
        floatingButton.visibility = View.VISIBLE
        //Crea un escuchador para el botón flotante que abre el formulario de creacion
        floatingButton.setOnClickListener {
            onAddButtonClick()
        }
    }

    /**
     * Al iniciar el fragmento se inicia el escuchador del adapter
     */
    override fun onStart() {
        super.onStart()
        (recyclerAdapter as FirestoreAdapter).startListening()
    }

    /**
     * Al parar el fragmento se para el escuchador del adapter
     */
    override fun onStop() {
        super.onStop()
        (recyclerAdapter as FirestoreAdapter).stopListening()
    }

    fun configRecyclerView(recyclerReference: RecyclerView) {
        recyclerView = recyclerReference
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true) //Tamaño fijo para mejorar el rendimiento

    }

    /**
     * Crea un fragmento de detalle
     * @return Fragmento de detalle de tipo DetailFormModelFragment
     */
    abstract fun getDetailFragment(): DetailFormModelFragment

    /**
     * Crea un objeto de tipo T a partir de un documento de la base de datos
     * @param documentSnapshot Documento de la base de datos
     * @return Objeto de tipo T
     */
    //abstract fun createObject(documentSnapshot: DocumentSnapshot?): T*/

    override fun onItemSelected(documentSnapshot: DocumentSnapshot?) {
        val detailFragment = getDetailFragment()
        detailFragment.documentSnapshot = documentSnapshot
        detailFragment.setAddFragment()
        fragmentReplacer(detailFragment, parentFragmentManager)
    }

    /**
     * Al pulsar el botón flotante se abre el fragmento de creación
     */
    open fun onAddButtonClick() {
        //Crea un fragment de añadir y lo reemplaza por el actual
        val detailFragment = getDetailFragment()
        detailFragment.setAddFragment()
        fragmentReplacer(detailFragment, parentFragmentManager)
    }

    /**
     * Configura los listeners de la vista de busqueda
     */
    open fun setSearchViewListeners() {
        with(this) {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                /**
                 * Se ejecuta cuando se pulsa el botón de buscar
                 * @param searchViewText Texto de la vista de busqueda
                 * @return Booleano que indica si se ha consumido el evento
                 */
                override fun onQueryTextSubmit(searchViewText: String?): Boolean {
                    if (searchViewText.isNullOrEmpty()) {
                        getAllDataFromDatabase()
                    } else {
                        filterDataFromString(searchViewText)
                    }
                    return false
                }

                /**
                 * Se ejecuta cada vez que se modifica el texto de la vista de busqueda
                 * @param newText Nuevo texto de la vista de busqueda
                 * @return Booleano que indica si se ha consumido el evento
                 */
                override fun onQueryTextChange(newText: String?): Boolean {
                    /* if (newText.isNullOrEmpty()){
                     getAllDataFromDatabase()}
                 else{
                     filterDataWithSearchString(newText)
                 }*/
                    return false
                }
            })
        }
    }

    /**
     * Recupera todos los datos de la base de datos y ejecuta la función updateData que se encarga de actualizar los datos del adaptador
     */
    open fun getAllDataFromDatabase() {
        dbFirestoreReference.get()
            .addOnSuccessListener { task ->
                updateRecyclerViewAdapterFromDocumentList(task.documents as ArrayList<DocumentSnapshot>)
            }
    }

    /**
     * Filtra los datos de la base de datos y los pasa al adaptador
     * @param searchString String de búsqueda
     */
    open fun filterDataFromString(searchString: String): ArrayList<DocumentSnapshot> {
        val queryList = generateFilteredItemListFromString(searchString)
        val tempList = ArrayList<DocumentSnapshot>()
        queryList.forEach { query ->
            query.get()
                .addOnSuccessListener { task ->
                    task.documents.forEach { document ->
                        tempList.add(document)
                    }
                }
        }
        return tempList
    }

}

/**
 * Interfaz para implementar como se comportará al hacer click a una ficha
 */
interface RecyclerAdapterListener {
    /**
     * Función que determina que hacer al hacer click a una ficha
     * @param documentSnapshot Parámetro que contiene la instancia
     */
    fun onItemSelected(documentSnapshot: DocumentSnapshot?)

    /**
     * Actualiza los datos del adaptador a partir de una lista de documentos
     * @param documentSnapshots Lista de documentos a partir de los que se actualiza el adaptador
     */
    fun updateRecyclerViewAdapterFromDocumentList(documentSnapshots: java.util.ArrayList<DocumentSnapshot>)

    /**
     * Genera una lista de filtros a partir de un string de búsqueda
     * @param searchString String de búsqueda
     * @return Lista de filtros
     */
    fun generateFilteredItemListFromString(searchString: String): List<Query>

}

