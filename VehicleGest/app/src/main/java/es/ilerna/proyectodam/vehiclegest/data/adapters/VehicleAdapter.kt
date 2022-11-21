package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle



class VehicleAdapter(
    query: Query,
    private val listener: VehicleAdapterListener
) : FirestoreAdapter<VehicleAdapter.VehicleViewHolder>(query) {

    class VehicleViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val cardView: MaterialCardView = itemView.findViewById(R.id.vehicle_card)
        private val title: TextView = itemView.findViewById(R.id.text_vehicles)

        fun bind(snapshot: DocumentSnapshot, listener: VehicleAdapterListener) {
            val vehicle: Vehicle? = snapshot.toObject(Vehicle::class.java)
            cardView.setOnClickListener {
                listener.onSportSelected(vehicle)
            }
        }
    }

    interface VehicleAdapterListener {
        fun onSportSelected(vehicle: Vehicle?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VehicleBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, listener, context)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }
}