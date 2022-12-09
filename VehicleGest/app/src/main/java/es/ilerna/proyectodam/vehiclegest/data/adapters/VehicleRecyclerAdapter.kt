package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.VehicleCardBinding
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class VehicleRecyclerAdapter(
    query: Query,
    private val listener: VehicleAdapterListener
) : FirestoreAdapter<VehicleRecyclerAdapter.VehicleViewHolder>(query) {

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class VehicleViewHolder(
        private val binding: VehicleCardBinding,

        ) : ViewHolder(binding.root) {

        private lateinit var progressBar: ProgressBar

        /**
         * Rellena cada item de la tarjeta con los datos del objeto vehiculo
         *
         */
        fun bind(
            snapshot: DocumentSnapshot,
            listener: VehicleAdapterListener
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
                    //Vehiclegest.displayImgURL(vehicle?.photoURL.toString(), binding.vehicleImage)

                    // Mostrar la barra de carga
                    progressBar = ProgressBar(Vehiclegest.appContext())
                    //Carga la foto en el formulario a partir de la URL almacenada
                    Controller().showImageFromUrl(
                        binding.vehicleImage,
                        vehicle?.photoURL.toString(),
                        progressBar
                    )

                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    binding.vehicleCard.setOnClickListener {
                        listener.onVehicleSelected(snapshot)
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
        fun onVehicleSelected(s: DocumentSnapshot?)
    }

    /**
     * Llamada para devolver el item al viewholder por cada objeto de la lista
     */
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VehicleCardBinding.inflate(layoutInflater, parent, false)
        return VehicleViewHolder(binding)
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     */
    @Override
    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }

}