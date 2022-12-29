package es.ilerna.proyectodam.vehiclegest.ui.inspections

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
import es.ilerna.proyectodam.vehiclegest.adapters.ITVRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentInspectionBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer

/**
 * Fragmento de listado de itv
 */
class ItvFragment : Fragment(), DataHelper.AdapterListener {

    //Enlaza el fragmento al xml
    private var _binding: FragmentInspectionBinding? = null
    private val binding get() = _binding!!

    //Crea una variable para el adaptador
    private lateinit var itvRecyclerAdapter: ITVRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var itvQuery: Query

    /**
     * Fase de creación del fragmento
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Consulta a firestore db de la colección de vehiculos
        itvQuery = Firebase.firestore.collection("ITV")

        //Crea un escuchador para el botón flotante que abre el formulario de creacion
        activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener() {
            onAddButtonClick()
        }

    }

    /**
     * Fase de creación de la vista
     */
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
        itvRecyclerAdapter = ITVRecyclerAdapter(itvQuery, this)
        //Asigna ese adapter al recyclerview
        recyclerView.adapter = itvRecyclerAdapter

        return root
    }

    //Al seleccionar un item de la lista se abre el fragmento de detalle
    override fun onItemSelected(snapshot: DocumentSnapshot?) {
        fragmentReplacer(ItvDetail(snapshot!!), parentFragmentManager)
    }

    //Al pulsar el botón flotante se abre el fragmento de creación
    override fun onAddButtonClick() {
        fragmentReplacer(AddItv(), parentFragmentManager)
    }

    /**
     * Al iniciar el fragmento se inicia el escuchador del adapter
     */
    override fun onStart() {
        super.onStart()
        itvRecyclerAdapter.startListening()
    }

    /**
     * Al parar el fragmento se para el escuchador del adapter
     */
    override fun onStop() {
        super.onStop()
        itvRecyclerAdapter.stopListening()
    }

    /**
     * Al destruir el fragmento se destruye el binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}