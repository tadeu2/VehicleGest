package es.ilerna.proyectodam.vehiclegest.ui.vehicles

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
import es.ilerna.proyectodam.vehiclegest.adapters.VehicleRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentVehiclesBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.ModelFragment
import org.checkerframework.checker.units.qual.s

/**
 * Fragmento de listado de vehículos
 */
class VehiclesFragment : Fragment(), DataHelper.AdapterListener {

    //Enlaza el fragmento con el xml
    private var _binding: FragmentVehiclesBinding? = null
    private val binding get() = _binding!!

    //Crea una variable para el adaptador
    private lateinit var vehicleRecyclerAdapter: VehicleRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var vehiclesQuery: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Consulta a firestore db de la colección de vehiculos
        vehiclesQuery = Firebase.firestore.collection("vehicle")

        //Crea un escuchador para el botón flotante que abre el formulario de creacion
        activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener() {
            onAddButtonClick()
        }

    }

    /**
     * Fase de creación de la vista
     * @param inflater Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Estado de la instancia
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Enlaza el fragmento a el xml y lo infla
        _binding = FragmentVehiclesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Pintar el recyclerview
        //Enlaza el recycler a la variable
        recyclerView = binding.recyclerVehicles
        //Le asigna un manager lineal en el contexto de este fragmento
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
        vehicleRecyclerAdapter = VehicleRecyclerAdapter(vehiclesQuery, this)
        //Asigna ese adapter al recyclerview
        recyclerView.adapter = vehicleRecyclerAdapter

        return root
    }

    //Al seleccionar un item de la lista se abre el fragmento de detalle
    override fun onItemSelected(snapshot: DocumentSnapshot?) {
        fragmentReplacer(VehicleDetail(snapshot!!), parentFragmentManager)
    }

    override fun onAddButtonClick() {
        fragmentReplacer(AddVehicle(), parentFragmentManager)
    }

    /**
     * Al iniciar la vista, inicializa el escuchador del adaptador
     */
    override fun onStart() {
        super.onStart()
        vehicleRecyclerAdapter.startListening()
    }

    /**
     * Al parar la vista, para el escuchador del adaptador
     */
    override fun onStop() {
        super.onStop()
        vehicleRecyclerAdapter.stopListening()
    }

    /**
     * Al destruir la vista, elimina el enlace con el xml
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}

