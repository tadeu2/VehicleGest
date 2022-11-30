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
import es.ilerna.proyectodam.vehiclegest.data.adapters.EmployeeRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.data.entities.Employee
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentEmployeesBinding
import es.ilerna.proyectodam.vehiclegest.ui.employees.EmployeeDetail

class EmployeeFragment : Fragment(), EmployeeRecyclerAdapter.EmployeeAdapterListener {

    private var _binding: FragmentEmployeesBinding? = null
    private val binding get() = _binding!!

    private lateinit var employeeQuery: Query

    private lateinit var employeeRecyclerAdapter: EmployeeRecyclerAdapter
    private lateinit var recyclerView: RecyclerView

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
        recyclerView = binding.recyclerEmployees
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        employeeRecyclerAdapter = EmployeeRecyclerAdapter(employeeQuery, this)
        recyclerView.adapter = employeeRecyclerAdapter

        return root
    }

    override fun onEmployeeSelected(employee: Employee?) {
        val deviceFragment = EmployeeDetail(employee!!)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, deviceFragment)
        fragmentTransaction.commit()
    }

    override fun onStart() {
        super.onStart()
        employeeRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        employeeRecyclerAdapter.startListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}