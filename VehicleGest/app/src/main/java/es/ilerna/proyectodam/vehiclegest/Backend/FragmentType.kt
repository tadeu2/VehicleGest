package es.ilerna.proyectodam.vehiclegest.Backend

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentVehiclesBinding
import es.ilerna.proyectodam.vehiclegest.data.adapters.VehicleRecyclerViewAdapter

class FragmentType {

    /**
     * Enumerador de nombres de los fragmentos
     */
    enum class SectionIdEnum(val id: Int) {
        VEHICLES(1), ITV(2), SERVICES(3), INVENTORY(4), EMPLOYEES(5)
    }

    class FragmentCommon(
        val vehicleListener: VehicleListenerImpl,
        val context: Context,
        val binding: FragmentVehiclesBinding
    ) {
        private lateinit var mAdapterVehicles: VehicleRecyclerViewAdapter
        fun createRecyclerView(vehicles: List<Vehicle>) {
            mAdapterVehicles =
                VehicleRecyclerViewAdapter(
                    vehicles as MutableList<Vehicle>,
                    vehicleListener,
                    context
                )
            val recyclerView = binding.recyclerVehicles
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = mAdapterVehicles
                addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            }
        }
    }
}