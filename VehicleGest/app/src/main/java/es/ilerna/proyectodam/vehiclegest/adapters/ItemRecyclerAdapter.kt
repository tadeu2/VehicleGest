package es.ilerna.proyectodam.vehiclegest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.ItemCardBinding
import es.ilerna.proyectodam.vehiclegest.models.Item
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class ItemRecyclerAdapter(
    query: Query,
    private val listener: ItemAdapterListener
) : FirestoreAdapter<ItemRecyclerAdapter.ItemViewHolder>(query) {


    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class ItemViewHolder(
        private val binding: ItemCardBinding,

        ) : ViewHolder(binding.root) {

        private lateinit var progressBar: ProgressBar

        /**
         * Rellena cada item de la tarjeta con los datos del objeto vehiculo
         */
        fun bind(
            snapshot: DocumentSnapshot,
            listener: ItemAdapterListener
        ) {
            try {
                //Crea un hilo paralelo para descargar las imagenes de una URL
                val executor = Executors.newSingleThreadExecutor()
                executor.execute {

                    //Inicializamos un objeto a partir de una instántanea
                    val item: Item? = snapshot.toObject(Item::class.java)

                    //La asignamos a los datos del formulario
                    binding.plateNumber.text = item?.plateNumber.toString()
                    binding.name.text = item?.name.toString()

                    // Mostrar la barra de carga
                    progressBar = ProgressBar(Vehiclegest.appContext())
                    //Carga la foto en el formulario a partir de la URL almacenada
                    Controller().showImageFromUrl(
                        binding.itemImage,
                        item?.photoURL.toString(),
                        progressBar
                    )
                    //Carga la foto en el formulario a partir de la URL almacenada
                    //Vehiclegest.displayImgURL(item?.photoURL.toString(), binding.itemImage)

                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    binding.itemCard.setOnClickListener {
                        listener.onItemSelected(snapshot)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface ItemAdapterListener {
        fun onItemSelected(snapshot: DocumentSnapshot?)
    }

    /**
     * Llamada para devolver el item(ItemCard) al viewholder por cada objeto de la lista vehiculos
     *
     */
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     */
    @Override
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }

}