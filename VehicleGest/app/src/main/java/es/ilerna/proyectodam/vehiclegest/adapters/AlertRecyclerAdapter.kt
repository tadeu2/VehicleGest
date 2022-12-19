package es.ilerna.proyectodam.vehiclegest.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.AlertCardBinding
import es.ilerna.proyectodam.vehiclegest.models.Alert
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param query Parámetro que contiene la consulta a la base de datos
 * @param listener Parámetro que contiene el listener del adapter
 */
class AlertRecyclerAdapter(
    query: Query,
    private val listener: AlertAdapterListener
) : FirestoreAdapter<AlertRecyclerAdapter.AlertViewHolder>(query) {

    /**
     * Clase interna
     * El holder se encarga de pintar las tarjetas de alerta
     * Implementa a RecyclerView.ViewHolder
     * @param binding Parámetro que contiene la vista de la tarjeta
     */
    class AlertViewHolder(
        private val binding: AlertCardBinding,

        ) : ViewHolder(binding.root) {

        /**
         * Función que se encarga de pintar los datos en la tarjeta
         * @param snapshot Parámetro que contiene la instancia de la alerta
         * @param listener Parámetro que contiene el listener de la tarjeta
         */
        fun bindDataCard(
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
                    binding.date.text = alert?.date?.let {
                        SimpleDateFormat(
                            Vehiclegest.instance.getString(R.string.dateFormat),
                            Locale.getDefault()
                        ).format(it)
                    }
                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    binding.alertCard.setOnClickListener {
                        listener.onAlertSelected(snapshot)
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
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface AlertAdapterListener {
        //Función que se encarga de abrir la ficha de la alerta
        fun onAlertSelected(snapshot: DocumentSnapshot?)

        //Función que se encarga de añadir un registro de alerta
        fun onAddButtonClick()
    }

    /**
     * Función que se encarga de crear el holder
     * @param parent Parámetro que contiene el ViewGroup
     * @param viewType Parámetro que contiene el tipo de vista
     * @return Devuelve el holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        return AlertViewHolder(
            //Infla la vista de la tarjeta
            AlertCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Función que se encarga de enlazar los datos con el holder
     * @param holder Parámetro que contiene el holder
     * @param position Parámetro que contiene la posición del holder
     */
    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        //Obtiene la instancia de la alerta
        getSnapshot(position)?.let { snapshot ->
            //Pinta los datos en la tarjeta
            holder.bindDataCard(snapshot, listener)
        }
    }
}