package es.ilerna.proyectodam.vehiclegest.ui.services

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
import es.ilerna.proyectodam.vehiclegest.adapters.ServiceRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentServicesBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer

/**
 * Fragmento de listado de servicios
 */
class ServiceFragment : Fragment(), Controller.AdapterListener {

    //Variables de fragmento
    private var fragmentServicesBinding: FragmentServicesBinding? = null
    private val getFragmentServicesBinding
        get() = fragmentServicesBinding ?: throw IllegalStateException("Binding is null")

    //Variables de Firebase
    private lateinit var serviceRecyclerAdapter: ServiceRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceCollectionReference: CollectionReference //Consulta de firestore

    /**
     * Fase de creación del fragmento
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {

            //Consulta a firestore db de la colección de vehiculos
            serviceCollectionReference = Firebase.firestore.collection("service")

            //Crea un escuchador para el botón flotante que abre el formulario de creacion
            activity?.findViewById<FloatingActionButton>(R.id.addButton)
                ?.setOnClickListener {
                    onAddButtonClick()
                }
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

            //Pintar el recyclerview
            //Enlaza el recycler a la variable
            recyclerView = getFragmentServicesBinding.recyclerservices
            //Le asigna un manager lineal en el contexto de este fragmento
            recyclerView.layoutManager = LinearLayoutManager(context) //contexto de este fragmento
            recyclerView.setHasFixedSize(true) //Para que no se recargue el tamaño del recycler
            //Le asigna un adaptador al recycler
            serviceRecyclerAdapter = ServiceRecyclerAdapter(serviceCollectionReference, this)
            recyclerView.adapter = serviceRecyclerAdapter //Asigna el adaptador al recycler
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
        return getFragmentServicesBinding.root
    }

    /**
     * Al pulsar el botón flotante se abre el fragmento de creación
     */
    override fun onAddButtonClick() {
        fragmentReplacer(ServiceAdder(), parentFragmentManager)
    }

    /**
     * Al pulsar el botón de editar se abre el fragmento de edición
     * @param documentSnapshot Documento de firestore que se va a editar
     */
    override fun onItemSelected(documentSnapshot: DocumentSnapshot?) {
        fragmentReplacer(ServiceDetail(documentSnapshot!!), parentFragmentManager)
    }

    /**
     *  Al iniciar el fragmento se inicia el escuchador del recycler
     */
    override fun onStart() {
        super.onStart()
        serviceRecyclerAdapter.startListening()
    }

    /**
     * Al parar el fragmento se detiene el escuchador del recycler
     */
    override fun onStop() {
        super.onStop()
        serviceRecyclerAdapter.stopListening()
    }

    /**
     * Al destruirse el fragmento se elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentServicesBinding = null
    }

}