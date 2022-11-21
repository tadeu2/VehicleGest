package es.ilerna.proyectodam.vehiclegest.Backend

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentVehiclesBinding
import es.ilerna.proyectodam.vehiclegest.ui.adapters.VehicleRecyclerViewAdapter
import es.ilerna.proyectodam.vehiclegest.Backend.VehicleListener

class FragmentType {

    /**
     * Enumerador de nombres de los fragmentos
     */
    enum class NavFragmentEnum(val id: Int) {
        VEHICLES(1), ITV(2), SERVICES(3), INVENTORY(4), Employees(5)
    }

    class FragmentCommon(
        val vehicleListener: VehicleListener,
        val context: Context,
        val binding: FragmentVehiclesBinding
    ) {
        private lateinit var mAdapterDispositivos: VehicleRecyclerViewAdapter
        fun createRecyclerView(dispositivos: List<Vehicle>) {
            mAdapterDispositivos =
                VehicleRecyclerViewAdapter(
                    dispositivos as MutableList<Vehicle>,
                    vehicleListener,
                    context
                )
            val recyclerView = binding.recyclerVehicles
            recyclerView.apply {
                //EL RECYCLER VIEW VA A SER UNA LISTA VERTICAL
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = mAdapterDispositivos
                addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            }
        }
    }
}