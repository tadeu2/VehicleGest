package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.VehicleCardBinding

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class VehicleRecyclerAdapter(
    query: Query,
    private val listener: VehicleAdapterListener
) : FirestoreAdapter<VehicleRecyclerAdapter.VehicleViewHolder>(query) {

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class VehicleViewHolder(
        private val binding: VehicleCardBinding,

        ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Rellena cada item de la lista bindeada
         * @param vehicleData Ficha de cada vehículo
         */
        fun bind(snapshot: DocumentSnapshot, listener: VehicleAdapterListener) {
            val vehicle: Vehicle? = snapshot.toObject(Vehicle::class.java)
            binding.vehicleCard.setOnClickListener {
                listener.onVehicleSelected(vehicle)
            }
            binding.plateNumber.text = vehicle?.plateNumber.toString()
            binding.type.text = vehicle?.type.toString()
            binding.brand.text = vehicle?.brand.toString()
            binding.model.text = vehicle?.model.toString()

            //binding.expirydateitv.text  = vehicleData.expiryDateITV
            //binding.totaldistance.text = vehicle?.totalDistance.toString()
        }
    }

    interface VehicleAdapterListener {
        fun onVehicleSelected(vehicle: Vehicle?)
    }

    /**
     * Llamada para devolver el item(VehicleCard) al viewholder por cada objeto de la lista vehiculos
     *
     */
    @NonNull
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VehicleCardBinding.inflate(layoutInflater, parent, false)
        return VehicleViewHolder(binding)
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     */
    @Override
    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }

    /**
    /**
     * Devuelve el tamaño del listado de vehículos
    */
    @Override
    override fun getItemCount() = query

     */
}