package es.ilerna.proyectodam.vehiclegest.ui.alerts


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.backend.ModelFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.data.adapters.AlertRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentAlertsBinding

/**
 * Fragmento de listado de alertas
 */
class AlertsFragment : ModelFragment(), AlertRecyclerAdapter.AlertAdapterListener {

    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!

    private lateinit var alertRecyclerAdapter: AlertRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var AlertsQuery: Query

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Pintar el fragment
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //Firestore
        AlertsQuery = FirebaseFirestore.getInstance().collection("alert")

        //Pintar el recycler
        recyclerView = binding.recycleralerts
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        alertRecyclerAdapter = AlertRecyclerAdapter(AlertsQuery, this)
        recyclerView.adapter = alertRecyclerAdapter

        return root
    }

    override fun onAlertSelected(s: DocumentSnapshot?) {
        val deviceFragment = AlertDetail(s!!)
        fragmentReplacer(deviceFragment, parentFragmentManager)
    }


    override fun onStart() {
        super.onStart()
        alertRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        alertRecyclerAdapter.startListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}