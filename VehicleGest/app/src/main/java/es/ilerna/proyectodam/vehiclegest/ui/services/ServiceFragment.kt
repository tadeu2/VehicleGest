package es.ilerna.proyectodam.vehiclegest.ui.services

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.adapters.ServiceRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentServicesBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.FragmentModel

/**
 * Fragmento de listado de servicios
 */
class ServiceFragment : FragmentModel() {

    //Variables de fragmento
    private var fragmentServicesBinding: FragmentServicesBinding? = null
    private val getFragmentServicesBinding
        get() = fragmentServicesBinding ?: throw IllegalStateException("Binding is null")

    /**
     * Fase de creación del fragmento
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {

            //Consulta a firestore db de la colección de vehiculos
            dbFirestoreReference = Firebase.firestore.collection("service")
            searchStringList = listOf("date", "plateNumber", "costumer")

            //Obtener el recycler y el adaptador
            recyclerAdapter = ServiceRecyclerAdapter(dbFirestoreReference, this)

        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
    }

    /**
     *  Fase de creación de la vista
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Pintar el fragment
            fragmentServicesBinding = FragmentServicesBinding.inflate(inflater, container, false)

            configRecyclerView(getFragmentServicesBinding.recyclerservices)
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
        return getFragmentServicesBinding.root
    }

    /**
     * Crea un fragmento de detalle
     * @return Fragmento de detalle de tipo DetailFormModelFragment
     */
    override fun getDetailFragment(): DetailFormModelFragment = ServiceDetailFragment()

    /**
     * Al destruirse el fragmento se elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentServicesBinding = null
    }
}