package es.ilerna.proyectodam.vehiclegest.ui.inspections

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
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.adapters.ITVRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentInspectionBinding
import es.ilerna.proyectodam.vehiclegest.ui.services.AddService

/**
 * Fragmento de listado de itv
 */
class ItvFragment : ModelFragment(), ITVRecyclerAdapter.ITVAdapterListener {

    private var _binding: FragmentInspectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var ITVRecyclerAdapter: ITVRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var itvQuery: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Consulta a firestore db de la colección de vehiculos
        itvQuery = Firebase.firestore.collection("ITV")

        //Crea un escuchador para el botón flotante que abre el formulario de creacion
        activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener() {
            onAddButtonClick()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        //Pintar el fragment
        _binding = FragmentInspectionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Pintar el recyclerview
        //Enlaza el recycler a la variable
        recyclerView = binding.recycleritv
        //Le asigna un manager lineal en el contexto de este fragmento
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
        ITVRecyclerAdapter = ITVRecyclerAdapter(itvQuery, this)
        //Asigna ese adapter al recyclerview
        recyclerView.adapter = ITVRecyclerAdapter

        return root
    }

    //Al seleccionar un item de la lista se abre el fragmento de detalle
    override fun onITVSelected(s: DocumentSnapshot?) {
        Vehiclegest.fragmentReplacer(ItvDetail(s!!), parentFragmentManager)
    }

    override fun onAddButtonClick() {
        Vehiclegest.fragmentReplacer(AddItv(), parentFragmentManager)
    }

    override fun onStart() {
        super.onStart()
        ITVRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        ITVRecyclerAdapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}