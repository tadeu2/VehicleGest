package es.ilerna.proyectodam.vehiclegest.ui.alerts


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.adapters.AlertRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.data.entities.Alert
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityMainBinding
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentAlertsBinding
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity

/**
 * Fragmento de listado de alertas
 */
class AlertsFragment : Fragment(), AlertRecyclerAdapter.AlertAdapterListener {

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

    override fun onAlertSelected(Alert: Alert?) {
        val deviceFragment = AlertDetail(Alert!!)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, deviceFragment)
        fragmentTransaction.commit()
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