package es.ilerna.proyectodam.vehiclegest.ui.services

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
import es.ilerna.proyectodam.vehiclegest.adapters.ServiceRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentServicesBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer

/**
 * Fragmento de listado de servicios
 */
class ServiceFragment : Fragment(), DataHelper.AdapterListener {

    //Variables de fragmento
    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding!!

    //Variables locales
    private lateinit var serviceRecyclerAdapter: ServiceRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceQuery: Query

    /**
     * Fase de creación del fragmento
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Consulta a firestore db de la colección de vehiculos
        serviceQuery = Firebase.firestore.collection("service")

        //Crea un escuchador para el botón flotante que abre el formulario de creacion
        activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener {
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
        _binding = FragmentServicesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Pintar el recyclerview
        //Enlaza el recycler a la variable
        recyclerView = binding.recyclerservices
        //Le asigna un manager lineal en el contexto de este fragmento
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        serviceRecyclerAdapter = ServiceRecyclerAdapter(serviceQuery, this)
        recyclerView.adapter = serviceRecyclerAdapter

        return root
    }

    /**
     * Al pulsar el botón flotante se abre el fragmento de creación
     */
    override fun onAddButtonClick() {
        fragmentReplacer(AddService(), parentFragmentManager)
    }

    /**
     * Al pulsar el botón de editar se abre el fragmento de edición
     */
    override fun onItemSelected(snapshot: DocumentSnapshot?) {
        fragmentReplacer(ServiceDetail(snapshot!!), parentFragmentManager)
    }

    /**
     *  Al iniciar el fragmento se inicia el escuchador del recycler
     */
    override fun onStart() {
        super.onStart()
        serviceRecyclerAdapter.startListening()
    }

    /**
     * Al parar el fragmento se detiene el escuchador del recycler
     */
    override fun onStop() {
        super.onStop()
        serviceRecyclerAdapter.stopListening()
    }

    /**
     * Al destruirse el fragmento se elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }

}