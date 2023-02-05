package es.ilerna.proyectodam.vehiclegest.interfaces

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
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
    lateinit var mainBinding: MainActivity

    lateinit var dbFirestoreReference: CollectionReference

    lateinit var searchStringList: List<String> //Lista de campos de busqueda

    //Fase de creación de la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //Obtiene los datos de la base de datos
            getDataFromDatabase(null)
            mainBinding = activity as MainActivity
            //Inicializa las variables y esconde barras de navegación pasándole las referencias
            initializeUI()

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
        mainBinding.activityMainBinding.apply {
            arrayOf(
                topBarMain.topToolbar,
                bottomBarMain.bottomNavMenu,
                searchView,
                contentMain.addButton
            ).forEach {
                it.visibility = View.VISIBLE
            }

            //Crea un escuchador para el botón flotante que abre el formulario de creacion
            contentMain.addButton.setOnClickListener {
                onAddButtonClick()
            }

            setSearchViewListeners() //Añade los escuchadores de la barra de búsqueda
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
        mainBinding.activityMainBinding.apply {
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
                    if (newText.isNullOrEmpty()) {
                        getDataFromDatabase(null)
                    }
                    return false
                }
            })
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


