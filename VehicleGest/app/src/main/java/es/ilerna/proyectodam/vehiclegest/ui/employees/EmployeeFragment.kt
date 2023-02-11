package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.adapters.EmployeeRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentEmployeesBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.FragmentModel

/**
 * Fragmento de listado de empleados
 */
class EmployeeFragment : FragmentModel() {

    //Inicializamos el binding con el XML de la interfaz
    private var fragmentEmployeesBinding: FragmentEmployeesBinding? = null
    private val getFragmentEmployeesBinding
        get() = fragmentEmployeesBinding ?: throw IllegalStateException("Binding error")

    /**
     * Crea un fragmento de detalle de empleado
     * @return Fragmento de detalle de empleado
     */
    override fun getDetailFragment(): DetailFormModelFragment =
        EmployeeDetailFragment()

    /**
     * Fase de creación del fragmento
     * @param savedInstanceState Estado de la instancia
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //Referencia a la base de datos de Firebase
            dbFirestoreReference = Firebase.firestore.collection("employees")
            searchStringList = listOf("dni", "surname", "email") //Lista de campos de busqueda

        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
    }

    /**
     * Fase de creación de la vista
     * @param inflater Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Estado de la instancia
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza el fragmento a el xml y lo infla
            fragmentEmployeesBinding = FragmentEmployeesBinding.inflate(inflater, container, false)

            //Enlaza el recycler al adaptador
            recyclerAdapter = EmployeeRecyclerAdapter(dbFirestoreReference, this)

            //Configura el recycler view con un layout manager y un adaptador
            configRecyclerView(getFragmentEmployeesBinding.recyclerEmployees)

        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
        }
        return getFragmentEmployeesBinding.root
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