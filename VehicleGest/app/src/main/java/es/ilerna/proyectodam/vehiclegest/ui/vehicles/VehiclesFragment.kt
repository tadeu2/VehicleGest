package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.os.Bundle
import android.os.ParcelUuid
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentVehiclesBinding
import es.ilerna.proyectodam.vehiclegest.data.adapters.VehicleRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle

/**
 * Fragmento de listado de vehículos
 */
class VehiclesFragment : Fragment(),VehicleRecyclerAdapter.VehicleAdapterListener {

    private var _binding: FragmentVehiclesBinding? = null
    private val binding get() = _binding!!

    private lateinit var vehicleRecyclerAdapter:VehicleRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var vehiclesQuery: Query


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Consulta a firestore db de la colección de vehiculos
        vehiclesQuery = Firebase.firestore.collection("vehicle")
    }
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
        vehicleRecyclerAdapter = VehicleRecyclerAdapter(vehiclesQuery,this)
        //Asigna ese adapter al recyclerview
        recyclerView.adapter = vehicleRecyclerAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener(){

        }

    }

    override fun onVehicleSelected(uuid: ParcelUuid,vehicle: Vehicle?) {
        val vehicleFragment = VehicleDetail(uuid, vehicle!!)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, vehicleFragment)
        fragmentTransaction.commit()
    }

    override fun onAddButtonClick(vehicle: Vehicle) {
/*        val vehicleFragment = VehicleDetail(vehicle!!)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, vehicleFragment)
        fragmentTransaction.commit()*/
    }

    override fun onStart() {
        super.onStart()
        vehicleRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        vehicleRecyclerAdapter.startListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}

