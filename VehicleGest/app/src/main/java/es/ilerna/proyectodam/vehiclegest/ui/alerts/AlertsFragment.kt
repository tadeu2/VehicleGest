package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.os.Bundle
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
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer

/**
 * Fragmento de listado de alertas
 */
class AlertsFragment : Fragment(), DataHelper.AdapterListener {

    //Enlaza el fragmento al xml
    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!

    //Crea una variable para el adaptador
    private lateinit var alertRecyclerAdapter: AlertRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var alertsQuery: CollectionReference //Consulta de firestore

    /**
     * Fase de creación del fragmento
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Consulta a firestore db de la colección
        alertsQuery = Firebase.firestore.collection("alert")

        //Crea un escuchador para el botón flotante que abre el formulario de creacion
        activity?.findViewById<FloatingActionButton>(R.id.addButton)?.setOnClickListener() {
            onAddButtonClick()
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

        //Enlaza el fragmento a el xml y lo infla
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Pintar el recyclerview
        //Enlaza el recycler a la variable
        recyclerView = binding.recycleralerts
        //Le asigna un manager lineal en el contexto de este fragmento
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
        alertRecyclerAdapter = AlertRecyclerAdapter(alertsQuery, this)
        //Asigna ese adapter al recyclerview
        recyclerView.adapter = alertRecyclerAdapter

        return root
    }

    //Al seleccionar un item de la lista se abre el fragmento de detalle
    override fun onItemSelected(snapshot: DocumentSnapshot?) {
        fragmentReplacer(AlertDetail(snapshot!!), parentFragmentManager)
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

    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}