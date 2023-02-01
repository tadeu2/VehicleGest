package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.adapters.VehicleRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentVehiclesBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.FragmentModel

/**
 * Fragmento de listado de vehículos
 */
class VehiclesFragment : FragmentModel() {

    //Enlaza el fragmento con el xml
    private var fragmentVehiclesBinding: FragmentVehiclesBinding? = null
    private val getFragmentVehiclesBinding
        get() = fragmentVehiclesBinding ?: throw IllegalStateException("Binding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //Referencia a la base de datos de Firebase
            dbFirestoreReference = Firebase.firestore.collection("vehicle")
            searchStringList = listOf("plateNumber", "model", "brand")

        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
        }
    }

    /**
     * Fase de creación de la vista
     * @param inflater Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Estado de la instancia
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Enlaza el fragmento a el xml y lo infla
        fragmentVehiclesBinding = FragmentVehiclesBinding.inflate(inflater, container, false)

        //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
        recyclerAdapter = VehicleRecyclerAdapter(dbFirestoreReference, this)

        configRecyclerView(getFragmentVehiclesBinding.recyclerVehicles)
        return getFragmentVehiclesBinding.root
    }

    /**
     * Crea un fragmento de detalle
     * @return Fragmento de detalle
     */
    override fun getDetailFragment(): DetailFormModelFragment = VehicleDetailFragment()

    /**
     * Al destruir la vista, elimina el enlace con el xml
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentVehiclesBinding = null
    }
}

