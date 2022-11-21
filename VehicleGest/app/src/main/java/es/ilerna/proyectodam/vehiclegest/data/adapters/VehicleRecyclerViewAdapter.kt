package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.util.Util.getSnapshot
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.*
import es.ilerna.proyectodam.vehiclegest.Backend.VehicleListenerImpl
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.VehicleCardBinding


/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class VehicleRecyclerViewAdapter(
    private val listVehicle: List<Vehicle>,
    private val vehiclelistener: VehicleListenerImpl,
    val context: Context
) : RecyclerView.Adapter<VehicleRecyclerViewAdapter.VehicleViewHolder>(),
    EventListener<QuerySnapshot> {

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class VehicleViewHolder(
        private val binding: VehicleCardBinding,
        private val listener: VehicleAdapterListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val cardView: MaterialCardView = itemView.findViewById(R.id.vehicle_card)
        private val title: TextView = itemView.findViewById(R.id.text_vehicles)

        /**
         * Rellena cada item de la lista bindeada
         * @param vehicleData Ficha de cada vehículo
         */
        fun render(snapshot: DocumentSnapshot, listener: VehicleAdapterListener) {
            val vehicle: Vehicle? = snapshot.toObject(Vehicle::class.java)
            cardView.setOnClickListener {
                listener.onVehicleSelected(vehicle)
            }
            binding.plateNumber.text = vehicle?.plateNumber.toString()
            binding.brand.text = vehicle?.brand.toString()
            binding.model.text = vehicle?.model.toString()
            binding.type.text = vehicle?.type.toString()
            //binding.expirydateitv.text  = vehicleData.expiryDateITV
            binding.totaldistance.text = vehicle?.totalDistance.toString()
        }
    }

    /**
     * Llamada para devolver el item(VehicleCard) al viewholder por cada objeto de la lista vehiculos
     *
     */
    @NonNull
    @Override
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): VehicleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VehicleCardBinding.inflate(layoutInflater, parent, false)
        return VehicleViewHolder(binding,VehicleAdapterListener)
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     */
    @Override
    override fun onBindViewHolder(@NonNull vehicleViewHolder: VehicleViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }

    /**
     * Devuelve el tamaño del listado de vehículos
     */
    @Override
    override fun getItemCount() = listVehicle.size

}

interface VehicleAdapterListener {
    fun onVehicleSelected(vehicle: Vehicle?)
}
