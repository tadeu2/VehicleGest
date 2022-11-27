package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Pintar el fragment
        _binding = FragmentVehiclesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //Firestore
        vehiclesQuery = FirebaseFirestore.getInstance().collection("vehicle")

        //Pintar el recycler
        recyclerView = binding.recyclerVehicles
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        vehicleRecyclerAdapter = VehicleRecyclerAdapter(vehiclesQuery,this)
        recyclerView.adapter = vehicleRecyclerAdapter

        return root
    }

    override fun onVehicleSelected(vehicle: Vehicle?) {
        val deviceFragment = VehicleDetail(vehicle!!)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, deviceFragment)
        fragmentTransaction.commit()
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

