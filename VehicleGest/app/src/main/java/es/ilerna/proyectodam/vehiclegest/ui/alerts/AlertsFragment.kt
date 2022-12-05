package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.ModelFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.data.adapters.AlertRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentAlertsBinding

/**
 * Fragmento de listado de alertas
 */
class AlertsFragment : ModelFragment(), AlertRecyclerAdapter.AlertAdapterListener {

    //Variables de fragmento
    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!

    private lateinit var alertRecyclerAdapter: AlertRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var alertsQuery: Query


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Consulta a firestore db de la colección
        alertsQuery = Firebase.firestore.collection("alert")

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
    override fun onAlertSelected(s: DocumentSnapshot?) {
        fragmentReplacer(AlertDetail(s!!), parentFragmentManager)
    }

    override fun onAddButtonClick() {
        fragmentReplacer(AddAlert(), parentFragmentManager)
    }

    //Inicia el escuchador de los cambios en la lista de instantáneas
    override fun onStart() {
        super.onStart()
        alertRecyclerAdapter.startListening()
    }

    //Para de escuchar los cambios
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