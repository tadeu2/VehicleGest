package es.ilerna.proyectodam.vehiclegest.ui.vehicles

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
import es.ilerna.proyectodam.vehiclegest.adapters.VehicleRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentVehiclesBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer

/**
 * Fragmento de listado de vehículos
 */
class VehiclesFragment : Fragment(), Controller.AdapterListener {

    //Enlaza el fragmento con el xml
    private var fragmentVehiclesBinding: FragmentVehiclesBinding? = null
    private val getFragmentVehiclesBinding
        get() = fragmentVehiclesBinding ?: throw IllegalStateException("Binding is null")

    //Crea una variable para el adaptador
    private lateinit var vehicleRecyclerAdapter: VehicleRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var vehiclesCollectionReference: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //Referencia a la base de datos de Firebase
            vehiclesCollectionReference = Firebase.firestore.collection("vehicles")
            //Crea un escuchador para el botón flotante que abre el formulario de creacion
            activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener {
                onAddButtonClick()
            }
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

        //Pintar el recyclerview
        //Enlaza el recycler a la variable
        recyclerView = getFragmentVehiclesBinding.recyclerVehicles
        //Le asigna un manager lineal en el contexto de este fragmento
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true) //Para que no se recargue el recycler al cambiar de tamaño

        //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
        vehicleRecyclerAdapter = VehicleRecyclerAdapter(vehiclesCollectionReference, this)
        //Asigna ese adapter al recyclerview
        recyclerView.adapter = vehicleRecyclerAdapter

        return getFragmentVehiclesBinding.root
    }

    /**
     * Al seleccionar un elemento del recycler se abre el formulario de edición
     * @param documentSnapshot Documento seleccionado
     */
    override fun onItemSelected(documentSnapshot: DocumentSnapshot?) {
        fragmentReplacer(VehicleDetail(documentSnapshot!!), parentFragmentManager)
    }

    /**
     * Al pulsar el botón flotante se abre el formulario de creación
     */
    override fun onAddButtonClick() {
        fragmentReplacer(AddVehicle(), parentFragmentManager)
    }

    /**
     * Al iniciar la vista, inicializa el escuchador del adaptador
     */
    override fun onStart() {
        super.onStart()
        vehicleRecyclerAdapter.startListening()
    }

    /**
     * Al parar la vista, para el escuchador del adaptador
     */
    override fun onStop() {
        super.onStop()
        vehicleRecyclerAdapter.stopListening()
    }

    /**
     * Al destruir la vista, elimina el enlace con el xml
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentVehiclesBinding = null
    }
}

