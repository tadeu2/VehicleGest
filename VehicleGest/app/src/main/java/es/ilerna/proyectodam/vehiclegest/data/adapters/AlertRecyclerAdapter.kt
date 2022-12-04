package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Alert
import es.ilerna.proyectodam.vehiclegest.databinding.AlertCardBinding
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
            val alert: Alert? = snapshot.toObject(Alert::class.java)
            assignData(alert, listener)
        }

        /**
         * Rellena cada item de la tarjeta con los datos del objeto alerta
         * @param Alert Ficha de cada alerta
         */
        private fun assignData(alert: Alert?, listener: AlertAdapterListener) {
            binding.plateNumber.text = alert?.plateNumber.toString()
            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            binding.date.text = alert?.date?.let { Vehiclegest.customDateFormat(it) }
            binding.alertCard.setOnClickListener {
                listener.onAlertSelected(alert)
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
     * Llamada para devolver el item(AlertCard) al viewholder por cada objeto de la lista alertas
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