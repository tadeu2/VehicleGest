package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.data.entities.ITV
import es.ilerna.proyectodam.vehiclegest.databinding.ItvCardBinding

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class ITVRecyclerAdapter(
    query: Query,
    private val listener: ITVAdapterListener
) : FirestoreAdapter<ITVRecyclerAdapter.ITVViewHolder>(query) {

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class ITVViewHolder(
        private val binding: ItvCardBinding,

        ) : ViewHolder(binding.root) {

        fun bind(snapshot: DocumentSnapshot, listener: ITVAdapterListener) {
            val ITV: ITV? = snapshot.toObject(ITV::class.java)
            assignData(ITV, listener)
        }

        /**
         * Rellena cada ITV de la tarjeta con los datos del objeto vehiculo
         * @param ITV Ficha de cada vehículo
         */
        private fun assignData(ITV: ITV?, listener: ITVAdapterListener) {
            binding
            binding.itvCard.setOnClickListener {
                listener.onITVSelected(ITV)
            }
        }
    }


    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface ITVAdapterListener {
        fun onITVSelected(itv: ITV?)
    }


    /**
     * Llamada para devolver el ITV(ITVCard) al viewholder por cada objeto de la lista vehiculos
     *
     */
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ITVViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItvCardBinding.inflate(layoutInflater, parent, false)
        return ITVViewHolder(binding)
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     */
    @Override
    override fun onBindViewHolder(holder: ITVViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }

}