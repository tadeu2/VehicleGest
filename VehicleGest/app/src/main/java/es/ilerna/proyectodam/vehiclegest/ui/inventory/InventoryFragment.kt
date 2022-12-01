package es.ilerna.proyectodam.vehiclegest.ui.inventory

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
import es.ilerna.proyectodam.vehiclegest.data.adapters.ItemRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.data.entities.Item
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentInventoryBinding

class InventoryFragment : Fragment(), ItemRecyclerAdapter.ItemAdapterListener {

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var ItemQuery: Query

    private lateinit var ItemRecyclerAdapter: ItemRecyclerAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Pintar el fragment
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //Firestore
        ItemQuery = FirebaseFirestore.getInstance().collection("inventory")

        //Pintar el recycler
        recyclerView = binding.recycleritems
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        ItemRecyclerAdapter = ItemRecyclerAdapter(ItemQuery, this)
        recyclerView.adapter = ItemRecyclerAdapter

        return root
    }

    override fun onItemSelected(Item: Item?) {
        val deviceFragment = ItemDetail(Item!!)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, deviceFragment)
        fragmentTransaction.commit()
    }

    override fun onStart() {
        super.onStart()
        ItemRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        ItemRecyclerAdapter.startListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}