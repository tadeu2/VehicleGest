package es.ilerna.proyectodam.vehiclegest.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.adapters.ItemRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentInventoryBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.ModelFragment

/**
 * Fragmento de listado de inventario
 */
class InventoryFragment : Fragment(), DataHelper.AdapterListener {

    //Inicializamos el binding con el XML de la interfaz
    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!

    //Variables locales
    private lateinit var itemRecyclerAdapter: ItemRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemQuery: Query

    //Fase de creación del fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Consulta a firestore db de la colección de vehiculos
        itemQuery = Firebase.firestore.collection("inventory")

        //Crea un escuchador para el botón flotante que abre el formulario de creacion
        activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener() {
            onAddButtonClick()
        }

    }

    /**
     *  Fase de creación de la vista
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Pintar el fragment
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Enlaza el recycler a la variable
        recyclerView = binding.recycleritems
        //Le asigna un manager lineal en el contexto de este fragmento
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
        itemRecyclerAdapter = ItemRecyclerAdapter(itemQuery, this)
        //Asigna ese adapter al recyclerview
        recyclerView.adapter = itemRecyclerAdapter

        return root
    }

    /**
     * Abre el formulario de creación de vehículos
     * @param snapshot Instanntanea del documento
     */
    override fun onItemSelected(snapshot: DocumentSnapshot?) {
        fragmentReplacer(ItemDetail(snapshot!!), parentFragmentManager)
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
        _binding = null

    }
}