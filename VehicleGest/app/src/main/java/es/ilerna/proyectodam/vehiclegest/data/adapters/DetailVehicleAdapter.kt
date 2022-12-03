package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.DetailVehicleBinding
import es.ilerna.proyectodam.vehiclegest.databinding.VehicleCardBinding
import es.ilerna.proyectodam.vehiclegest.ui.vehicles.VehicleDetail
import es.ilerna.proyectodam.vehiclegest.ui.vehicles.VehiclesFragment

/**
 * Interfaz para crear escuchadores para las diferentes entidades de la base de datos Firestore
 */
class DetailVehicleAdapter(
    query: Query,
    private val listener: VehicleDetailAdapterListener
) : FirestoreAdapter<DetailVehicleAdapter.VehicleDetailViewHolder>(query) {

    class VehicleDetailViewHolder(
        private val binding: DetailVehicleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(snapshot: DocumentSnapshot, listener: VehicleDetailAdapterListener) {
            val vehicle: Vehicle? = snapshot.toObject(Vehicle::class.java)
            assignData(vehicle, listener)
        }

        fun assignData(vehicle : Vehicle?, listener: VehicleDetailAdapterListener) {

        }

    }

    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha o al botón de añadir
     */
    interface VehicleDetailAdapterListener {
        fun onVehicleSelected(vehicle: Vehicle?)
        fun onAddButtonClick(vehicle:Vehicle)
    }


    /**
     * Llamada para devolver el item(VehicleCard) al viewholder por cada objeto de la lista vehiculos
     */
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DetailVehicleBinding.inflate(layoutInflater, parent, false)
        return VehicleDetailViewHolder(binding)
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     */
    @Override
    override fun onBindViewHolder(holder: VehicleDetailViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bindData(snapshot, listener)
        }
    }

}


