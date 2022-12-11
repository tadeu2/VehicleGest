package es.ilerna.proyectodam.vehiclegest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.AlertCardBinding
import es.ilerna.proyectodam.vehiclegest.models.Alert
import java.util.concurrent.Executors

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

        fun bind(
            snapshot: DocumentSnapshot,
            listener: AlertAdapterListener
        ) {
            try {
                //Crea un hilo paralelo para descargar las imagenes de una URL
                val executor = Executors.newSingleThreadExecutor()
                executor.execute {
                    //Inicializamos un objeto a partir de una instántanea
                    val alert: Alert? = snapshot.toObject(Alert::class.java)
                    binding.plateNumber.text = alert?.plateNumber.toString()
                    //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
                    //El formato se puede modificar en strings.xml
                    binding.date.text = alert?.date?.let { Vehiclegest.customDateFormat(it) }

                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    binding.alertCard.setOnClickListener {
                        listener.onAlertSelected(snapshot)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface AlertAdapterListener {
        fun onAlertSelected(s: DocumentSnapshot?)
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