package es.ilerna.proyectodam.vehiclegest.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.AlertCardBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.RecyclerAdapterListener
import es.ilerna.proyectodam.vehiclegest.models.Alert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param queryFirestoreDatabase Parámetro que contiene la consulta a la base de datos
 * @param recyclerAdapterListener Parámetro que contiene el listener del adapter
 */
class AlertRecyclerAdapter(
    queryFirestoreDatabase: Query,
    private val recyclerAdapterListener: RecyclerAdapterListener
) : FirestoreAdapter<AlertRecyclerAdapter.AlertViewHolder>(queryFirestoreDatabase) {
    /**
     * Clase interna
     * El holder se encarga de pintar las tarjetas de alerta
     * Implementa a RecyclerView.ViewHolder
     * @param alertCardBinding Parámetro que contiene la vista de la tarjeta
     */
    class AlertViewHolder(
        private val alertCardBinding: AlertCardBinding,
    ) : RecyclerView.ViewHolder(alertCardBinding.root) {
        /**
         * Función que se encarga de pintar los datos en la tarjeta
         * @param documentSnapshot Parámetro que contiene la instancia de la alerta
         * @param recyclerAdapterListener Parámetro que contiene el listener de la tarjeta
         */
        fun bindDataToCardView(
            documentSnapshot: DocumentSnapshot,
            recyclerAdapterListener: RecyclerAdapterListener
        ) {
            try {
                //Crea un hilo paralelo para descargar las imagenes de una URL
                CoroutineScope(Dispatchers.Main).launch {

                    //Inicializamos un objeto a partir de una instántanea
                    val alert: Alert? = documentSnapshot.toObject(Alert::class.java)
                    alertCardBinding.plateNumber.text = alert?.plateNumber.toString()

                    //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
                    //El formato se puede modificar en strings.xml
                    alertCardBinding.date.text = alert?.date?.let {
                        SimpleDateFormat(
                            Vehiclegest.instance.getString(R.string.dateFormat),
                            Locale.getDefault()
                        ).format(it)
                    }
                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    alertCardBinding.alertCard.setOnClickListener {
                        recyclerAdapterListener.onItemSelected(documentSnapshot)
                    }
                }
            } catch (exception: Exception) {
                Log.e("Error", exception.message.toString(), exception)
                exception.printStackTrace()
            } catch (nullPointerException: NullPointerException) {
                Log.e("Error", "Null reference exception", nullPointerException)
                nullPointerException.printStackTrace()
            }

        }
    }

    /**
     * Función que se encarga de crear el holder
     * @param viewGroup Parámetro que contiene el ViewGroup
     * @param viewType Parámetro que contiene el tipo de vista
     * @return Devuelve el holder creado con la vista de la tarjeta
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlertViewHolder {
        return AlertViewHolder(
            //Infla la vista de la tarjeta de alerta y la pasa al holder para que la pinte
            AlertCardBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    /**
     * Función que se encarga de enlazar los datos con el holder y pintarlos en la tarjeta de alerta
     * @param alertViewHolder Parámetro que contiene el holder de la tarjeta de alerta
     * @param position Parámetro que contiene la posición del holder en el recyclerview
     */
    override fun onBindViewHolder(alertViewHolder: AlertViewHolder, position: Int) {
        //Obtiene la instancia de la alerta en la posición indicada por el parámetro position
        getSnapshot(position)?.let { documentSnapshot ->
            //Pinta los datos en la tarjeta de la alerta en la posición indicada por el parámetro position
            alertViewHolder.bindDataToCardView(documentSnapshot, recyclerAdapterListener)
        }
    }
}