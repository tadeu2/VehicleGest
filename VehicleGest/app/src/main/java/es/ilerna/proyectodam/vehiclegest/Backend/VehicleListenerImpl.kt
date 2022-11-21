package es.ilerna.proyectodam.vehiclegest.Backend


import android.content.Context
import androidx.fragment.app.FragmentManager
import es.codigonline.proyecto.smarthome.ui.home.VehiclesViewModel
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle

class VehicleListenerImpl(
    val context: Context,
    val vehicleViewModel: VehiclesViewModel,
    private val fragmentManager: FragmentManager
) : VehicleListener {
    override fun addVehicle(id: Long) {
        TODO("Not yet implemented")
    }

    override fun delVehicle(id: Long) {
        TODO("Not yet implemented")
    }

    override fun addITV(id: Long) {
        TODO("Not yet implemented")
    }

    override fun delITV(id: Long) {
        TODO("Not yet implemented")
    }

    override fun delPersonal(id: Long) {
        TODO("Not yet implemented")
    }

    override fun details(dispositivoCompleto: Vehicle) {
        TODO("Not yet implemented")
    }

    override fun edit(dispositivoCompleto: Vehicle) {
        TODO("Not yet implemented")
    }

}