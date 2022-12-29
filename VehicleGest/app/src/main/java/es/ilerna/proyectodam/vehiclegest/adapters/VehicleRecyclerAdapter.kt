package es.ilerna.proyectodam.vehiclegest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.databinding.VehicleCardBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper
import es.ilerna.proyectodam.vehiclegest.models.Vehicle
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class VehicleRecyclerAdapter(
    query: Query,
    private val listener: DataHelper.AdapterListener
) : FirestoreAdapter<VehicleRecyclerAdapter.VehicleViewHolder>(query) {

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class VehicleViewHolder(
        private val binding: VehicleCardBinding,

        ) : ViewHolder(binding.root) {
        /**
         * Rellena cada item de la tarjeta con los datos del objeto vehiculo
         *
         */
        fun bindDataToCardview(
            snapshot: DocumentSnapshot,
            listener: DataHelper.AdapterListener
        ) {
            try {//Crea un hilo paralelo para descargar las imagenes de una URL
                val executor = Executors.newSingleThreadExecutor()

                executor.execute {
                    //Inicializamos un objeto vehículo a partir de una instántanea
                    val vehicle: Vehicle? = snapshot.toObject(Vehicle::class.java)
                    //La asignamos a los datos del formulario
                    binding.plateNumber.text = vehicle?.plateNumber.toString()
                    binding.type.text = vehicle?.type.toString()
                    binding.brand.text = vehicle?.brand.toString()
                    binding.model.text = vehicle?.model.toString()

                    //Carga la foto en el formulario a partir de la URL almacenada
                    Controller().showImageFromUrl(
                        binding.vehicleImage,
                        vehicle?.photoURL.toString(),
                    )

                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    binding.vehicleCard.setOnClickListener {
                        listener.onItemSelected(snapshot)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha o al botón de añadir
     */
    interface VehicleAdapterListener {
        /**
         * Acción al pulsar una ficha
         * @param snapshot Instántanea del documento
         */
        fun onVehicleSelected(snapshot: DocumentSnapshot?)

        /**
         * Acción al pulsar el botón de añadir
         */
        fun onAddButtonClick()
    }

    /**
     * Infla el layout de la tarjeta
     * @param parent ViewGroup padre de la vista
     * @param viewType Tipo de vista a inflar
     * @return Devuelve el holder de la tarjeta con el layout inflado y el listener
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        return VehicleViewHolder(
            VehicleCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     * @param holder Holder de la tarjeta a rellenar con los datos del vehículo
     * @param position Posición de la tarjeta a rellenar
     */
    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        //Obtiene la instántanea del documento
        getSnapshot(position)?.let { snapshot ->
            //Rellena la tarjeta con los datos del vehículo
            holder.bindDataToCardview(snapshot, listener)
        }
    }

}