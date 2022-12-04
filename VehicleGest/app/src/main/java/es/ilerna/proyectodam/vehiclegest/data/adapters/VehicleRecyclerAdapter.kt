package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.VehicleCardBinding
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class VehicleRecyclerAdapter(
    query: Query, private val listener: VehicleAdapterListener
) : FirestoreAdapter<VehicleRecyclerAdapter.VehicleViewHolder>(query) {

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class VehicleViewHolder(
        private val binding: VehicleCardBinding,

        ) : RecyclerView.ViewHolder(binding.root) {

        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null

        /**
         * Rellena cada item de la tarjeta con los datos del objeto vehiculo
         * @param vehicle Ficha de cada vehículo
         */
        fun bind(snapshot: DocumentSnapshot, listener: VehicleAdapterListener) {
            //Crea un hilo paralelo para descargar las imagenes de una URL
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                val vehicle: Vehicle? = snapshot.toObject(Vehicle::class.java)
                binding.plateNumber.text = vehicle?.plateNumber.toString()
                binding.type.text = vehicle?.type.toString()
                binding.brand.text = vehicle?.brand.toString()
                binding.model.text = vehicle?.model.toString()

                //Carga la foto en el formulario a partir de la URL almacenada
                val im = java.net.URL(vehicle?.photoURL).openStream()
                image = BitmapFactory.decodeStream(im)

                handler.post {
                    binding.vehicleImage.setImageBitmap(image)
                }
                binding.vehicleCard.setOnClickListener {
                    Log.d("", "");
                    listener.onVehicleSelected(snapshot)
                }
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
     * Llamada para devolver el item(VehicleCard) al viewholder por cada objeto de la lista vehiculos
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