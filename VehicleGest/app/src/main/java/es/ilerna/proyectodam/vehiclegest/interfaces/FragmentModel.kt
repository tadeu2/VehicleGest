package es.ilerna.proyectodam.vehiclegest.interfaces

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.adapters.FirestoreAdapter
import es.ilerna.proyectodam.vehiclegest.adapters.RecyclerAdapterListener
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity

/**
 * Fragmento de listado de empleados
 */
abstract class FragmentModel : Fragment(), RecyclerAdapterListener {

    //Creamos una variable para el adaptador
    lateinit var recyclerAdapter: RecyclerView.Adapter<*>

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchTopBar: MaterialToolbar//Barra de búsqueda superior

    //Variables que almacenarán las instancias de las barras de navegación y el bóton flotante
    private lateinit var topToolBar: MaterialToolbar //Barra de navegación superior
    private lateinit var navBarBot: BottomNavigationView //Barra de navegación inferior

    private lateinit var floatingButton: FloatingActionButton //Botón flotante de la interfaz
    lateinit var dbFirestoreReference: CollectionReference

    //Referencia a la vista de busqueda
    private lateinit var searchView: SearchView
    lateinit var searchStringList: List<String> //Lista de campos de busqueda

    //Fase de creación de la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //Obtiene los datos de la base de datos
             getDataFromDatabase(null)
            //Inicializa las variables y esconde barras de navegación pasándole las referencias
            initializeUI()
            //Con with(this) nos referimos a la clase que implementa el fragmento
            with(this) {

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
        topToolBar = requireActivity().findViewById(R.id.topToolbar)
        topToolBar.visibility = View.VISIBLE
        searchView = searchTopBar.menu.findItem(R.id.searchButton).actionView as SearchView
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = View.VISIBLE
        searchTopBar = requireActivity().findViewById(R.id.searchToolbar)
        searchTopBar.visibility = View.VISIBLE
        floatingButton = requireActivity().findViewById(R.id.addButton)
        floatingButton.visibility = View.VISIBLE
        //Crea un escuchador para el botón flotante que abre el formulario de creacion
        floatingButton.setOnClickListener {
            onAddButtonClick()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val menuHost = requireActivity() as MainActivity //Obtiene la actividad que aloja el fragmento
        //Añade un menú de opciones a la barra de navegación superior
        menuHost.addMenuProvider(object : MenuProvider {
            /**
             * Called by the [MenuHost] to allow the [MenuProvider]
             * to inflate [MenuItem]s into the menu.
             *
             * @param menu         the menu to inflate the new menu items into
             * @param menuInflater the inflater to be used to inflate the updated menu
             */
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_bar_items, menu)
            }

            /**
             * Called by the [MenuHost] when a [MenuItem] is selected from the menu.
             *
             * @param menuItem the menu item that was selected
             * @return `true` if the given menu item is handled by this menu provider,
             * `false` otherwise
             */
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                menuItem.apply {
                    when (itemId) {R.id.searchButton -> {
                            searchTopBar.visibility = View.VISIBLE
                            topToolBar.visibility = View.GONE
                            navBarBot.visibility = View.GONE
                            floatingButton.visibility = View.GONE
                            return true
                        }
                    }
                }
                return false
            }
        })
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

    /**
     * Configura el recycler view y el adaptador
     * @param recyclerReference Referencia al recycler view
     */
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
     * @return Objeto de tipo T
     */
    //abstract fun createObject(documentSnapshot: DocumentSnapshot?): T*/

    /**
     * Al pulsar un elemento de la lista se abre el fragmento de detalle
     * @param documentSnapshot Documento de la base de datos
     */
    override fun onItemSelected(documentSnapshot: DocumentSnapshot?) {
        val detailFragment = getDetailFragment()
        detailFragment.documentSnapshot = documentSnapshot
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
                    getDataFromDatabase(searchViewText)
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
            searchView.setOnCloseListener {
                getDataFromDatabase(null)
                false
            }
        }
    }

    /**
     * Recupera todos los datos de la base de datos y ejecuta la función updateData que se encarga de actualizar los datos del adaptador
     */
    open fun getDataFromDatabase(searchViewText: String?) {
        var documentSnapshotList: ArrayList<DocumentSnapshot>
        if (searchViewText.isNullOrEmpty()) {
            dbFirestoreReference.get()
                .addOnSuccessListener { task ->
                    documentSnapshotList = task.documents as ArrayList<DocumentSnapshot>
                    (recyclerAdapter as FirestoreAdapter).updateDocumentSnapshotData(
                        documentSnapshotList
                    )
                }
        } else {
            generateFilteredItemListFromString(searchViewText).addOnSuccessListener { result ->
                documentSnapshotList = result
                (recyclerAdapter as FirestoreAdapter).updateDocumentSnapshotData(
                    documentSnapshotList
                )
            }
        }
    }

    /**
     * Genera una lista de filtros a partir de un string de búsqueda
     * @param searchString String de búsqueda
     * @return Lista de filtros
     */
    private fun generateFilteredItemListFromString(searchString: String?): Task<ArrayList<DocumentSnapshot>> {
        val filteredItemList = ArrayList<DocumentSnapshot>()
        val listOfTasks = arrayListOf<Task<QuerySnapshot>>()
        for (it in searchStringList) {
            //listOfTasks.add(dbFirestoreReference.whereArrayContains(it, searchString!!).get())
            //listOfTasks.add(dbFirestoreReference.whereEqualTo(it, searchString).get())
            //listOfTasks.add(dbFirestoreReference.whereLessThan(it, searchString + "\uf8ff").get())
            listOfTasks.add(
                dbFirestoreReference
                    .whereGreaterThanOrEqualTo(it, searchString!!)
                    .whereLessThan(it, searchString + "\uf8ff").get()
            )
        }
        return Tasks.whenAllSuccess<QuerySnapshot>(listOfTasks)
            .continueWith { task ->
                for (querySnapshotTask in task.result) {
                    filteredItemList.addAll(querySnapshotTask.documents)
                }
                filteredItemList
            }
    }
}


