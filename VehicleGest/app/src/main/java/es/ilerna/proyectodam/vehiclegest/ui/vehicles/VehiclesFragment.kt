package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.vehicledata.VehicleDataProvider
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentVehiclesBinding
import es.ilerna.proyectodam.vehiclegest.data.adapters.VehicleRecyclerViewAdapter

/**
 * Fragmento de listado de vehículos
 */
class VehiclesFragment : Fragment(R.layout.fragment_vehicles) {

    private var _binding: FragmentVehiclesBinding? = null
    private val binding get() = _binding!!
/*
    //private val viewModel: VehiclesViewModel by viewModels()
    override fun onCreateView(
       // inflater: LayoutInflater,
        //container: ViewGroup?,
        savedInstanceState: Bundle?
    ){


        // _binding = FragmentVehiclesBinding.inflate(inflater, container, false)
        //  val root: View = binding.root
        /*
        viewModel.(TipoDispositivo.GENERALES).observe(viewLifecycleOwner) {
            CommonFragmentImpl(
                DeviceListenerImpl(
                    requireContext(),
                    viewModel,
                    parentFragmentManager
                ), requireContext(), binding
            ).createRecyclerView(it)
        }

*/
        // return root
    }
*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }
    private fun initRecyclerView() {
        val recyclerView = binding.recyclerVehicles
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = VehicleRecyclerViewAdapter(VehicleDataProvider.vehicleList)
    }
}
/*
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}*/
