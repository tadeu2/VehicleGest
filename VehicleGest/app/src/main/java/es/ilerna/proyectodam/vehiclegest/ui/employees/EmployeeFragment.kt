package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.adapters.EmployeeRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentEmployeesBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer

/**
 * Fragmento de listado de empleados
 */
class EmployeeFragment : Fragment(), Controller.AdapterListener {

    //Inicializamos el binding con el XML de la interfaz
    private var fragmentEmployeesBinding: FragmentEmployeesBinding? = null
    private val getFragmentEmployeesBinding
        get() = fragmentEmployeesBinding ?: throw IllegalStateException("Binding error")

    //Creamos una variable para el adaptador
    private lateinit var employeeRecyclerAdapter: EmployeeRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var employeeCollectionReference: CollectionReference

    //Fase de creación del fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //Referencia a la base de datos de Firebase
            employeeCollectionReference = Firebase.firestore.collection("employees")
            //Crea un escuchador para el botón flotante que abre el formulario de creacion
            activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener {
                onAddButtonClick()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza el fragmento a el xml y lo infla
            fragmentEmployeesBinding = FragmentEmployeesBinding.inflate(inflater, container, false)

            //Enlaza el recycler a la variable
            recyclerView = getFragmentEmployeesBinding.recyclerEmployees
            // Enlaza el layout manager al recyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true) //Tamaño fijo para mejorar el rendimiento

            employeeRecyclerAdapter = EmployeeRecyclerAdapter(employeeCollectionReference, this)
            recyclerView.adapter = employeeRecyclerAdapter
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
        }
        return getFragmentEmployeesBinding.root
    }

    /**
     * Al seleccionar un item de la lista se abre el fragmento de detalle
     * @param documentSnapshot Documento de la base de datos
     */
    override fun onItemSelected(documentSnapshot: DocumentSnapshot?) {
        fragmentReplacer(EmployeeDetail(documentSnapshot!!), parentFragmentManager)
    }

    /**
     * Al pulsar el botón flotante se abre el fragmento de creación
     */
    override fun onAddButtonClick() {
        fragmentReplacer(EmployeeAdder(), parentFragmentManager)
    }

    /**
     * Al iniciar el fragmento se inicia el escuchador del adapter
     */
    override fun onStart() {
        super.onStart()
        employeeRecyclerAdapter.startListening()
    }

    /**
     * Al parar el fragmento se para el escuchador del adapter
     */
    override fun onStop() {
        super.onStop()
        employeeRecyclerAdapter.stopListening()
    }

    /**
     * Fase de destrucción del fragmento se elimina el binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentEmployeesBinding = null
    }
}