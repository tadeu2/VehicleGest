package es.ilerna.proyectodam.vehiclegest.ui.inspections

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
import es.ilerna.proyectodam.vehiclegest.data.adapters.ITVRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.data.entities.ITV
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentItvBinding

class ITVFragment : Fragment(), ITVRecyclerAdapter.ITVAdapterListener {

    private var _binding: FragmentItvBinding? = null
    private val binding get() = _binding!!

    private lateinit var ITVQuery: Query

    private lateinit var ITVRecyclerAdapter: ITVRecyclerAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        //Pintar el fragment
        _binding = FragmentItvBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //Firestore
        ITVQuery = FirebaseFirestore.getInstance().collection("ITVs")

        //Pintar el recycler
        recyclerView = binding.recycleritv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        ITVRecyclerAdapter = ITVRecyclerAdapter(ITVQuery, this)
        recyclerView.adapter = ITVRecyclerAdapter

        return root
    }

    override fun onITVSelected(itv: ITV?) {
        val deviceFragment = ItvDetail(itv!!)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, deviceFragment)
        fragmentTransaction.commit()
    }

    override fun onStart() {
        super.onStart()
        ITVRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        ITVRecyclerAdapter.startListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}