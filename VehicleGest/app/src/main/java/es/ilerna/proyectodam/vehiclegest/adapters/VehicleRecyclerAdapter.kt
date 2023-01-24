package es.ilerna.proyectodam.vehiclegest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.databinding.VehicleCardBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.models.Vehicle
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param queryFirestoreDatabase Parámetro que contiene la consulta a la base de datos
 * @param adapterListener Parámetro que contiene el listener del adapter
 */
class VehicleRecyclerAdapter(
    queryFirestoreDatabase: Query,
    private val adapterListener: Controller.AdapterListener
) : FirestoreAdapter<VehicleRecyclerAdapter.VehicleViewHolder>(queryFirestoreDatabase) {

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class VehicleViewHolder(
        private val vehicleCardBinding: VehicleCardBinding,

        ) : RecyclerView.ViewHolder(vehicleCardBinding.root) {
        /**
         * Rellena cada item de la tarjeta con los datos del objeto vehiculo
         *
         */
        fun bindDataToCardview(
            documentSnapshot: DocumentSnapshot,
            adapterListener: Controller.AdapterListener
        ) {
            try {//Crea un hilo paralelo para descargar las imagenes de una URL
                val executorService = Executors.newSingleThreadExecutor()

                executorService.execute {
                    //Inicializamos un objeto vehículo a partir de una instántanea
                    val vehicle: Vehicle? = documentSnapshot.toObject(Vehicle::class.java)
                    //La asignamos a los datos del formulario
                    vehicleCardBinding.plateNumber.text = vehicle?.plateNumber.toString()
                    vehicleCardBinding.type.text = vehicle?.type.toString()
                    vehicleCardBinding.brand.text = vehicle?.brand.toString()
                    vehicleCardBinding.model.text = vehicle?.model.toString()

                    //Carga la foto en el formulario a partir de la URL almacenada
                    Controller().showImageFromUrl(
                        vehicleCardBinding.vehicleImage,
                        vehicle?.photoURL.toString(),
                    )

                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    vehicleCardBinding.vehicleCard.setOnClickListener {
                        adapterListener.onItemSelected(documentSnapshot)
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
     * @param vehicleViewHolder Holder de la tarjeta a rellenar con los datos del vehículo
     * @param position Posición de la tarjeta a rellenar
     */
    override fun onBindViewHolder(vehicleViewHolder: VehicleViewHolder, position: Int) {
        //Obtiene la instántanea del documento
        getSnapshot(position)?.let { documentSnapshot ->
            //Rellena la tarjeta con los datos del vehículo
            vehicleViewHolder.bindDataToCardview(documentSnapshot, adapterListener)
        }
    }

}