package es.ilerna.proyectodam.vehiclegest.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.ItvCardBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.ModelFragment
import es.ilerna.proyectodam.vehiclegest.models.ITV
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param query Parámetro que contiene la consulta a la base de datos
 *  @param listener Parámetro que contiene el listener del adapter
 */
class ITVRecyclerAdapter(
    query: Query, private val listener: ModelFragment
) : FirestoreAdapter<ITVRecyclerAdapter.ITVViewHolder>(query) {

    /**
     * Clase interna
     * El holder se encarga de pintar las tarjetas de ITV
     * Implementa a RecyclerView.ViewHolder
     * @param binding Parámetro que contiene la vista de la tarjeta
     */
    class ITVViewHolder(
        private val binding: ItvCardBinding,

        ) : ViewHolder(binding.root) {

        /**
         * Rellena cada ITV de la tarjeta con los datos del objeto vehiculo
         */
        fun bindDataCard(
            snapshot: DocumentSnapshot, listener: ModelFragment
        ) {
            try {
                //Crea un hilo paralelo para descargar las imagenes de una URL
                val executor = Executors.newSingleThreadExecutor()
                executor.execute {

                    //Inicializamos un objeto a partir de una instántanea
                    val itv: ITV? = snapshot.toObject(ITV::class.java)

                    //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
                    //El formato se puede modificar en strings.xml
                    binding.date.text = itv?.date?.let {
                        SimpleDateFormat(
                            Vehiclegest.instance.getString(R.string.dateFormat),
                            Locale.getDefault()
                        ).format(it)
                    }
                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    binding.itvCard.setOnClickListener {
                        listener.onItemSelected(snapshot)
                    }
                }

            } catch (e: Exception) {
                Log.e("Error", e.message.toString(), e)
                e.printStackTrace()
            } catch (e2: NullPointerException) {
                Log.e("Error", "Referencia nula", e2)
                e2.printStackTrace()
            }
        }
    }

    /**
     * Llamada para devolver el ITV(ITVCard) al viewholder por cada objeto de la lista vehiculos
     * @param parent Parámetro que contiene el ViewGroup
     * @param viewType Parámetro que contiene el tipo de vista
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ITVViewHolder {
        return ITVViewHolder(
            ItvCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Llamada para rellenar cada ITVCard con los datos del objeto
     * @param holder Parámetro que contiene la vista de la tarjeta
     * @param position Parámetro que contiene la posición de la tarjeta
     */
    override fun onBindViewHolder(holder: ITVRecyclerAdapter.ITVViewHolder, position: Int) {
        //Obtiene la instancia de la tarjeta
        getSnapshot(position)?.let { snapshot ->
            //Rellena la tarjeta con los datos del objeto
            holder.bindDataCard(snapshot, listener)
        }
    }

}
