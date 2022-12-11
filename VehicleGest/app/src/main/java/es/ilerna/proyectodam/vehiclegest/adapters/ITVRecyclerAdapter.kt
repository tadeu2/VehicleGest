package es.ilerna.proyectodam.vehiclegest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.ItvCardBinding
import es.ilerna.proyectodam.vehiclegest.models.ITV
import java.util.concurrent.Executors

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

        /**
         * Rellena cada ITV de la tarjeta con los datos del objeto vehiculo
         */
        fun bind(
            snapshot: DocumentSnapshot,
            listener: ITVAdapterListener
        ) {
            try {
                //Crea un hilo paralelo para descargar las imagenes de una URL
                val executor = Executors.newSingleThreadExecutor()
                executor.execute {

                    //Inicializamos un objeto a partir de una instántanea
                    val itv: ITV? = snapshot.toObject(ITV::class.java)

                    //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
                    //El formato se puede modificar en strings.xml
                    binding.date.text = itv?.date?.let { Vehiclegest.customDateFormat(it) }

                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    binding.itvCard.setOnClickListener {
                        listener.onITVSelected(snapshot)
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
    interface ITVAdapterListener {
        fun onITVSelected(snapshot: DocumentSnapshot?)
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
