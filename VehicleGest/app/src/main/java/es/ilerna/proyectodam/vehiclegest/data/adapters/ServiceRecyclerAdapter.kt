package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.data.entities.Service
import es.ilerna.proyectodam.vehiclegest.databinding.ServiceCardBinding

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class ServiceRecyclerAdapter(
    query: Query,
    private val listener: ServiceAdapterListener
) : FirestoreAdapter<ServiceRecyclerAdapter.ServiceViewHolder>(query) {

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class ServiceViewHolder(
        private val binding: ServiceCardBinding,

        ) : ViewHolder(binding.root) {

        fun bind(snapshot: DocumentSnapshot, listener: ServiceAdapterListener) {
            val Service: Service? = snapshot.toObject(Service::class.java)
            assignData(Service, listener)
        }

        /**
         * Rellena cada Service de la tarjeta con los datos del objeto vehiculo
         * @param Service Ficha de cada vehículo
         */
        private fun assignData(Service: Service?, listener: ServiceAdapterListener) {
            binding.plateNumber.text = Service?.plateNumber.toString()
            binding.serviceCard.setOnClickListener {
                listener.onServiceSelected(Service)
            }
        }
    }


    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface ServiceAdapterListener {
        fun onServiceSelected(Service: Service??)
    }


    /**
     * Llamada para devolver el Service(ServiceCard) al viewholder por cada objeto de la lista vehiculos
     *
     */
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ServiceCardBinding.inflate(layoutInflater, parent, false)
        return ServiceViewHolder(binding)
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     */
    @Override
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }

}