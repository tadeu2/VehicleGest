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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.adapters.EmployeeRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentEmployeesBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.FragmentModel
import es.ilerna.proyectodam.vehiclegest.models.Employee

/**
 * Fragmento de listado de empleados
 */
class EmployeeFragment : FragmentModel() {

    //Inicializamos el binding con el XML de la interfaz
    private var fragmentEmployeesBinding: FragmentEmployeesBinding? = null
    private val getFragmentEmployeesBinding
        get() = fragmentEmployeesBinding ?: throw IllegalStateException("Binding error")

    //Fase de creación del fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //Referencia a la base de datos de Firebase
            collectionReference = Firebase.firestore.collection("employees")

        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza el fragmento a el xml y lo infla
            fragmentEmployeesBinding = FragmentEmployeesBinding.inflate(inflater, container, false)

            //Enlaza el recycler al adaptador
            recyclerAdapter  = EmployeeRecyclerAdapter(collectionReference, this)

            //Configura el recycler view con un layout manager y un adaptador
            configRecyclerView(getFragmentEmployeesBinding.recyclerEmployees)

        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
        }
        return getFragmentEmployeesBinding.root
    }
    /**
     * Genera las consultas para filtrar los datos de la base de datos
     */
    override fun generateFilteredItemListFromString(searchString: String): List<Query> {
        val queryDni = collectionReference
            .whereGreaterThanOrEqualTo("dni", searchString)
            .whereLessThanOrEqualTo("dni", searchString + "\uf8ff")
        val querySurname = collectionReference
            .whereGreaterThanOrEqualTo("surname", searchString)
            .whereLessThanOrEqualTo("surname", searchString + "\uf8ff")
        return listOf(queryDni, querySurname)
    }

    override fun updateRecyclerViewAdapterFromDocumentList(documentSnapshots: ArrayList<DocumentSnapshot>) {
        (recyclerAdapter as EmployeeRecyclerAdapter).updateData(documentSnapshots) as EmployeeRecyclerAdapter
    }

    override fun getDetailFragment(item: DocumentSnapshot): Fragment = EmployeeDetailFragment(item)

    override fun getAdderFragment(): Fragment = EmployeeAdderFragment()


    /**
     * Fase de destrucción del fragmento se elimina el binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentEmployeesBinding = null
    }
}