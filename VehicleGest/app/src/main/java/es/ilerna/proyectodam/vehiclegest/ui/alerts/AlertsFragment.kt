package es.ilerna.proyectodam.vehiclegest.ui.alerts

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
import es.ilerna.proyectodam.vehiclegest.adapters.AlertRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentAlertsBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer

/**
 * Fragmento de listado de alertas
 */
class AlertsFragment : Fragment(), Controller.AdapterListener {

    //Enlaza el fragmento al xml
    private var _fragmentAlertsBinding: FragmentAlertsBinding? = null
    private val getFragmentAlertsBinding
        get() = _fragmentAlertsBinding ?: throw IllegalStateException("Binding error")

    //Crea una variable para el adaptador
    private lateinit var alertRecyclerAdapter: AlertRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var alertsCollectionReference: CollectionReference //Consulta de firestore

    /**
     * Fase de creación del fragmento
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //Referencia a la base de datos de Firebase
            alertsCollectionReference = Firebase.firestore.collection("alerts")
            //Crea un escuchador para el botón flotante que abre el formulario de creacion
            activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener() {
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
     *  @param savedInstanceState Estado de la instancia
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza el fragmento a el xml y lo infla
            _fragmentAlertsBinding = FragmentAlertsBinding.inflate(inflater, container, false)

            //Pintar el recyclerview
            //Enlaza el recycler a la variable
            recyclerView = getFragmentAlertsBinding.recycleralerts
            //Le asigna un manager lineal en el contexto de este fragmento
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)

            //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
            alertRecyclerAdapter = AlertRecyclerAdapter(alertsCollectionReference, this)
            //Asigna ese adapter al recyclerview
            recyclerView.adapter = alertRecyclerAdapter
        } catch (exception: Exception) {
            Log.w(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
        return getFragmentAlertsBinding.root
    }

    /**
     * Al seleccinar un elemento del recycler se abre el detalle
     * @param documentSnapshot Documento de firestore
     */
    override fun onItemSelected(documentSnapshot: DocumentSnapshot?) {
        fragmentReplacer(AlertDetail(documentSnapshot!!), parentFragmentManager)
    }

    /**
     * Al pulsar el botón flotante se abre el fragmento de creación
     */
    override fun onAddButtonClick() {
        fragmentReplacer(AddAlert(), parentFragmentManager)
    }

    /**
     * Al iniciar el fragmento el escuchador del adaptador se activa
     */
    override fun onStart() {
        super.onStart()
        alertRecyclerAdapter.startListening()
    }

    /**
     * Al parar el fragmento el escuchador del adaptador se desactiva
     */
    override fun onStop() {
        super.onStop()
        alertRecyclerAdapter.stopListening()
    }

    /**
     * Al destruir el fragmento se elimina la variable de enlace al xml
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _fragmentAlertsBinding = null
    }
}