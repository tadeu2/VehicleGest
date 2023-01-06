package es.ilerna.proyectodam.vehiclegest.ui.inventory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.adapters.ItemRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentInventoryBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer

/**
 * Fragmento de listado de inventario
 */
class InventoryFragment : Fragment(), Controller.AdapterListener {

    //Inicializamos el binding con el XML de la interfaz
    private var fragmentInventoryBinding: FragmentInventoryBinding? = null
    private val getfragmentInventoryBinding
        get() = fragmentInventoryBinding ?: throw IllegalStateException("Binding error")

    //Crea una variable para el adaptador
    private lateinit var itemRecyclerAdapter: ItemRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemCollectionReference: CollectionReference //Consulta de firestore

    /**
     * Fase de creación del fragmento
     * @param savedInstanceState Bundle de datos
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {

            //Referencia a la base de datos de Firebase
            itemCollectionReference = Firebase.firestore.collection("inventory")
            //Crea un escuchador para el botón flotante que abre el formulario de creacion
            activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener {
                onAddButtonClick()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    /**
     *  Fase de creación de la vista
     *  @param inflater  Inflador de la vista
     *  @param container Contenedor de la vista
     *  @param savedInstanceState Bundle de datos
     *  @return Vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    try {
        //Pintar el fragment
        fragmentInventoryBinding = FragmentInventoryBinding.inflate(inflater, container, false)

        //Enlaza el recycler a la variable
        recyclerView = getfragmentInventoryBinding.recycleritems
        //Le asigna un manager lineal en el contexto de este fragmento
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true) //Para que no se recargue la vista al hacer scroll

        //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
        itemRecyclerAdapter = ItemRecyclerAdapter(itemCollectionReference, this)
        //Asigna ese adapter al recyclerview
        recyclerView.adapter = itemRecyclerAdapter
    } catch (exception: Exception) {
        Log.e("InventoryFragment", exception.message.toString() ,exception)
        exception.printStackTrace()
    }
        return getfragmentInventoryBinding.root
    }

    /**
     * Abre el formulario de creación de vehículos
     * @param documentSnapshot Instanntanea del documento
     */
    override fun onItemSelected(documentSnapshot: DocumentSnapshot?) {
        fragmentReplacer(ItemDetail(documentSnapshot!!), parentFragmentManager)
    }

    /**
     * Abre el formulario de creación de items
     */
    override fun onAddButtonClick() {
        fragmentReplacer(AddItem(), parentFragmentManager)
    }

    /**
     * Al iniciar el fragmento, se inicia el escuchador del adapter
     */
    override fun onStart() {
        super.onStart()
        itemRecyclerAdapter.startListening()
    }

    /**
     * Al parar el fragmento, se para el escuchador del adapter
     */
    override fun onStop() {
        super.onStop()
        itemRecyclerAdapter.stopListening()
    }

    /**
     * Al destruir el fragmento, se destruye el binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentInventoryBinding = null

    }
}