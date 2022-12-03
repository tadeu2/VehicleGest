package es.ilerna.proyectodam.vehiclegest.ui.services

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
import es.ilerna.proyectodam.vehiclegest.data.adapters.ServiceRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.data.entities.Service
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentServicesBinding
import es.ilerna.proyectodam.vehiclegest.ui.services.ServiceDetail

class ServiceFragment : Fragment(), ServiceRecyclerAdapter.ServiceAdapterListener {

    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding!!

    private lateinit var serviceQuery: Query

    private lateinit var serviceRecyclerAdapter: ServiceRecyclerAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Pintar el fragment
        _binding = FragmentServicesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //Firestore
        serviceQuery = FirebaseFirestore.getInstance().collection("service")

        //Pintar el recycler
        recyclerView = binding.recyclerservices
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        serviceRecyclerAdapter = ServiceRecyclerAdapter(serviceQuery, this)
        recyclerView.adapter = serviceRecyclerAdapter

        return root
    }

    override fun onServiceSelected(Service: Service?) {
        val deviceFragment = ServiceDetail(Service!!)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, deviceFragment)
        fragmentTransaction.commit()
    }

    override fun onStart() {
        super.onStart()
        serviceRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        serviceRecyclerAdapter.startListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}