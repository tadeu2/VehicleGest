import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.adapters.EmployeeListAdapter
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentEmployeesBinding
import es.ilerna.proyectodam.vehiclegest.ui.vehicles.VehicleDetail

class EmployeeFragment : Fragment(), EmployeeListAdapter.EmployeeAdapterListener {

    private var _binding: FragmentEmployeesBinding? = null
    private val binding get() = _binding!!

    private lateinit var employeeQuery: Query

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Pintar el fragment
        _binding = FragmentEmployeesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //Firestore
        employeeQuery = FirebaseFirestore.getInstance().collection("employees")

        //Pintar el recycler
        var employeeList = binding.employeeItemList
        var employeeAdapter = EmployeeListAdapter(employeeQuery, this)
        employeeList.adapter = employeeAdapter

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