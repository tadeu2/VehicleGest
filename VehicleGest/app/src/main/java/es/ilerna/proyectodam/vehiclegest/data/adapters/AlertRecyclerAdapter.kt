package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Alert
import es.ilerna.proyectodam.vehiclegest.databinding.AlertCardBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class AlertRecyclerAdapter(
    query: Query,
    private val listener: AlertAdapterListener
) : FirestoreAdapter<AlertRecyclerAdapter.AlertViewHolder>(query) {

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class AlertViewHolder(
        private val binding: AlertCardBinding,

        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(snapshot: DocumentSnapshot, listener: AlertAdapterListener) {
            val Alert: Alert? = snapshot.toObject(Alert::class.java)
            assignData(Alert, listener)
        }

        /**
         * Rellena cada item de la tarjeta con los datos del objeto vehiculo
         * @param Alert Ficha de cada vehículo
         */
        private fun assignData(Alert: Alert?, listener: AlertAdapterListener) {
            binding.plateNumber.text = Alert?.plateNumber.toString()

            //Formatea los timestamp según el string de recursos.
            val simpleDateFormat = SimpleDateFormat(
                Vehiclegest.appContext().resources
                    .getString(R.string.dateFormat), Locale.getDefault()
            )
            val stamp = Alert?.date?.time
            val date = simpleDateFormat.format(Date(stamp!!))

            binding.date.text = date
            binding.alertCard.setOnClickListener {
                listener.onAlertSelected(Alert)
            }
        }
    }


    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface AlertAdapterListener {
        fun onAlertSelected(Alert: Alert?)
    }


    /**
     * Llamada para devolver el item(AlertCard) al viewholder por cada objeto de la lista vehiculos
     *
     */
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AlertCardBinding.inflate(layoutInflater, parent, false)
        return AlertViewHolder(binding)
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     */
    @Override
    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }

}