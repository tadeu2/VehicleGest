package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.adapters.EmployeeRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentEmployeesBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.ModelFragment

/**
 * Fragmento de listado de empleados
 */
class EmployeeFragment : ModelFragment(), EmployeeRecyclerAdapter.EmployeeAdapterListener {

    private var _binding: FragmentEmployeesBinding? = null
    private val binding get() = _binding!!

    private lateinit var employeeRecyclerAdapter: EmployeeRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var employeeQuery: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Consulta a firestore db de la colección
        employeeQuery = Firebase.firestore.collection("employees")

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

        //Enlaza el fragmento a el xml y lo infla
        _binding = FragmentEmployeesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //Firestore
        employeeQuery = FirebaseFirestore.getInstance().collection("employees")

        //Pintar el recyclerview
        //Enlaza el recycler a la variable
        recyclerView = binding.recyclerEmployees
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        employeeRecyclerAdapter = EmployeeRecyclerAdapter(employeeQuery, this)
        recyclerView.adapter = employeeRecyclerAdapter

        return root
    }

    //Al seleccionar un item de la lista se abre el fragmento de detalle
    override fun onEmployeeSelected(snapshot: DocumentSnapshot?) {
        fragmentReplacer(EmployeeDetail(snapshot!!), parentFragmentManager)
    }

    override fun onAddButtonClick() {
        fragmentReplacer(AddEmployee(), parentFragmentManager)
    }

    //Inicia el escuchador de los cambios en la lista de instantáneas
    override fun onStart() {
        super.onStart()
        employeeRecyclerAdapter.startListening()
    }

    //Para de escuchar los cambios
    override fun onStop() {
        super.onStop()
        employeeRecyclerAdapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}