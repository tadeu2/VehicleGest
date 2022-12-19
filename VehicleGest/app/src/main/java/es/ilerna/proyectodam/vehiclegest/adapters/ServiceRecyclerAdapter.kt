package es.ilerna.proyectodam.vehiclegest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.ServiceCardBinding
import es.ilerna.proyectodam.vehiclegest.models.Service
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param query Parámetro que contiene la consulta a la base de datos
 * @param listener Parámetro que contiene el listener del adapter
 */
class ServiceRecyclerAdapter(
    query: Query, private val listener: ServiceAdapterListener
) : FirestoreAdapter<ServiceRecyclerAdapter.ServiceViewHolder>(query) {

    /**
     * Clase interna
     * El holder se encarga de pintar las tarjetas
     * Implementa a RecyclerView.ViewHolder
     * @param binding Parámetro que contiene la vista de la tarjeta
     */
    class ServiceViewHolder(
        private val binding: ServiceCardBinding,

        ) : ViewHolder(binding.root) {

        /**
         * Rellena cada Service de la tarjeta con los datos del objeto
         */
        fun bind(
            snapshot: DocumentSnapshot, listener: ServiceAdapterListener
        ) {
            try {

                //Crea un hilo paralelo para descargar las imagenes de una URL
                val executor = Executors.newSingleThreadExecutor()
                executor.execute {

                    //Inicializamos un objeto a partir de una instántanea
                    val service = snapshot.toObject(Service::class.java)
                    //La asignamos a los datos del formulario
                    binding.plateNumber.text = service?.plateNumber.toString()
                    //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
                    //El formato se puede modificar en strings.xml
                    binding.date.text = service?.date?.let {
                        SimpleDateFormat(
                            Vehiclegest.instance.getString(R.string.dateFormat),
                            Locale.getDefault()
                        ).format(it)
                    }
                    binding.serviceCard.setOnClickListener {
                        listener.onServiceSelected(snapshot)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } catch (e2: NullPointerException) {
                e2.printStackTrace()
            }
        }
    }


    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface ServiceAdapterListener {
        /**
         * Función que se ejecuta al hacer click en una ficha
         * @param snapshot Parámetro que contiene la instancia del objeto
         */
        fun onServiceSelected(snapshot: DocumentSnapshot?)

        /**
         * Función que se ejecuta al hacer click en el botón de añadir
         */
        fun onAddServiceButtonClick()
    }


    /**
     * Llamada para devolver el Service(ServiceCard) al viewholder por cada objeto de la lista vehiculos
     * @param parent Parámetro que contiene el ViewGroup
     * @param viewType Parámetro que contiene el tipo de vista
     * @return Devuelve el ServiceViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        return ServiceViewHolder(
            ServiceCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     * @param holder Parámetro que contiene el ServiceViewHolder
     * @param position Parámetro que contiene la posición del objeto
     */
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        //Obtiene el objeto de la lista de servicios
        getSnapshot(position)?.let { snapshot ->
            //Llama al método bind del ServiceViewHolder para rellenar los datos
            holder.bind(snapshot, listener)
        }
    }

}