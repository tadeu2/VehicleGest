package es.ilerna.proyectodam.vehiclegest.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.ItvCardBinding
import es.ilerna.proyectodam.vehiclegest.models.ITV
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param queryFireStoreDatabase Parámetro que contiene la consulta a la base de datos
 *  @param adapterListener Parámetro que contiene el listener del adapter
 */
class ItvRecyclerAdapter(
    queryFireStoreDatabase: Query,
    private val adapterListener: Controller.AdapterListener
) : FirestoreAdapter<ItvRecyclerAdapter.ItvViewHolder>(queryFireStoreDatabase) {

    /**
     * Clase interna
     * El holder se encarga de pintar las tarjetas de ITV
     * Implementa a RecyclerView.ViewHolder
     * @param itvCardBinding Parámetro que contiene la vista de la tarjeta
     */
    class ItvViewHolder(
        private val itvCardBinding: ItvCardBinding,

        ) : RecyclerView.ViewHolder(itvCardBinding.root) {

        /**
         * Rellena cada ITV de la tarjeta con los datos del objeto vehiculo
         */
        fun bindDataToCardview(
            documentSnapshot: DocumentSnapshot, adapterListener: Controller.AdapterListener
        ) {
            try {
                //Crea un hilo paralelo para descargar las imagenes de una URL
                val executorService = Executors.newSingleThreadExecutor()
                executorService.execute {

                    //Inicializamos un objeto a partir de una instántanea
                    val itv: ITV? = documentSnapshot.toObject(ITV::class.java)

                    //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
                    //El formato se puede modificar en strings.xml
                    itvCardBinding.date.text = itv?.date?.let {
                        SimpleDateFormat(
                            Vehiclegest.instance.getString(R.string.dateFormat),
                            Locale.getDefault()
                        ).format(it)
                    }
                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    itvCardBinding.itvCard.setOnClickListener {
                        adapterListener.onItemSelected(documentSnapshot)
                    }
                }

            } catch (exception: Exception) {
                Log.e("Error", exception.message.toString(), exception)
                exception.printStackTrace()
            } catch (nullPointerException: NullPointerException) {
                Log.e("Error", "Referencia nula", nullPointerException)
                nullPointerException.printStackTrace()
            }
        }
    }

    /**
     * Llamada para devolver el ITV(ITVCard) al viewholder por cada objeto de la lista vehiculos
     * @param viewGroup Parámetro que contiene el ViewGroup
     * @param viewType Parámetro que contiene el tipo de vista
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItvViewHolder {
        return ItvViewHolder(
            ItvCardBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    /**
     * Llamada para rellenar cada ITVCard con los datos del objeto
     * @param itvViewHolder Parámetro que contiene la vista de la tarjeta
     * @param position Parámetro que contiene la posición de la tarjeta
     */
    override fun onBindViewHolder(itvViewHolder: ItvViewHolder, position: Int) {
        //Obtiene la instancia de la tarjeta
        getSnapshot(position)?.let { documentSnapshot ->
            //Rellena la tarjeta con los datos del objeto
            itvViewHolder.bindDataToCardview(documentSnapshot, adapterListener)
        }
    }

}
