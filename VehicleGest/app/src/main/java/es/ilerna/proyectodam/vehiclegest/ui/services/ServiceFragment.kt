package es.ilerna.proyectodam.vehiclegest.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.ModelFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.data.adapters.ServiceRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentServicesBinding

/**
 * Fragmento de listado de servicios
 */
class ServiceFragment : ModelFragment(), ServiceRecyclerAdapter.ServiceAdapterListener {

    //Variables de fragmento
    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding!!

    private lateinit var serviceRecyclerAdapter: ServiceRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceQuery: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Consulta a firestore db de la colección de vehiculos
        serviceQuery = Firebase.firestore.collection("service")

        //Crea un escuchador para el botón flotante que abre el formulario de creacion
        activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener() {
            onAddButtonClick()
        }

    }

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

    //Al seleccionar un item de la lista se abre el fragmento de detalle
    override fun onServiceSelected(snapshot: DocumentSnapshot?) {
        fragmentReplacer(ServiceDetail(snapshot!!), parentFragmentManager)
    }

    override fun onAddButtonClick() {
        fragmentReplacer(AddService(), parentFragmentManager)
    }

    //Inicia el escuchador de los cambios en la lista de instantáneas
    override fun onStart() {
        super.onStart()
        serviceRecyclerAdapter.startListening()
    }

    //Para de escuchar los cambios
    override fun onStop() {
        super.onStop()
        serviceRecyclerAdapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }

}