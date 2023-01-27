package es.ilerna.proyectodam.vehiclegest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.ServiceCardBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.RecyclerAdapterListener
import es.ilerna.proyectodam.vehiclegest.models.Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param queryFireStoreDatabase Parámetro que contiene la consulta a la base de datos
 * @param recyclerAdapterListener Parámetro que contiene el listener del adapter
 */
class ServiceRecyclerAdapter(
    queryFireStoreDatabase: Query,
    private val recyclerAdapterListener: RecyclerAdapterListener
) : FirestoreAdapter<ServiceRecyclerAdapter.ServiceViewHolder>(queryFireStoreDatabase) {

    /**
     * Clase interna
     * El holder se encarga de pintar las tarjetas
     * Implementa a RecyclerView.ViewHolder
     * @param serviceCardBinding Parámetro que contiene la vista de la tarjeta
     */
    class ServiceViewHolder(
        private val serviceCardBinding: ServiceCardBinding,

        ) : RecyclerView.ViewHolder(serviceCardBinding.root) {

        /**
         * Rellena cada Service de la tarjeta con los datos del objeto
         */
        fun bindDataToCardView(
            documentSnapshot: DocumentSnapshot,
            recyclerAdapterListener: RecyclerAdapterListener
        ) {
            try {

                //Crea un hilo paralelo para descargar las imagenes de una URL
                CoroutineScope(Dispatchers.Main).launch {

                    //Inicializamos un objeto a partir de una instántanea
                    val service = documentSnapshot.toObject(Service::class.java)
                    //La asignamos a los datos del formulario
                    serviceCardBinding.plateNumber.text = service?.plateNumber.toString()
                    //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
                    //El formato se puede modificar en strings.xml
                    serviceCardBinding.date.text = service?.date?.let {
                        SimpleDateFormat(
                            Vehiclegest.instance.getString(R.string.dateFormat), Locale.getDefault()
                        ).format(it)
                    }
                    serviceCardBinding.serviceCard.setOnClickListener {
                        recyclerAdapterListener.onItemSelected(documentSnapshot)
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            } catch (nullPointerException: NullPointerException) {
                nullPointerException.printStackTrace()
            }
        }
    }

    /**
     * Llamada para devolver el Service(ServiceCard) al viewholder por cada objeto de la lista vehiculos
     * @param viewGroup Parámetro que contiene el ViewGroup
     * @param viewType Parámetro que contiene el tipo de vista
     * @return Devuelve el ServiceViewHolder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ServiceViewHolder {
        return ServiceViewHolder(
            ServiceCardBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
        )
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     * @param serviceViewHolder Parámetro que contiene el ServiceViewHolder
     * @param position Parámetro que contiene la posición del objeto
     */
    override fun onBindViewHolder(serviceViewHolder: ServiceViewHolder, position: Int) {
        //Obtiene el objeto de la lista de servicios
        getSnapshot(position)?.let { documentSnapshot ->
            //Llama al método bind del ServiceViewHolder para rellenar los datos
            serviceViewHolder.bindDataToCardView(documentSnapshot, recyclerAdapterListener)
        }
    }

}