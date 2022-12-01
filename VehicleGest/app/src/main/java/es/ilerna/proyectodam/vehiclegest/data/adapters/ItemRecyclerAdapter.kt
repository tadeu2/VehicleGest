package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.data.entities.Item
import es.ilerna.proyectodam.vehiclegest.databinding.ItemCardBinding

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

        fun bind(snapshot: DocumentSnapshot, listener: ItemAdapterListener) {
            val item: Item? = snapshot.toObject(Item::class.java)
            assignData(item, listener)
        }

        /**
         * Rellena cada item de la tarjeta con los datos del objeto vehiculo
         * @param item Ficha de cada vehículo
         */
        private fun assignData(item: Item?, listener: ItemAdapterListener) {
            binding.plateNumber.text = item?.plateNumber.toString()
            binding.name.text = item?.name.toString()
            //Foto del articulo
            Glide.with(binding.root).load(item?.photoURL).into(binding.itemImage)

            binding.itemCard.setOnClickListener {
                listener.onItemSelected(item)
            }
        }
    }


    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface ItemAdapterListener {
        fun onItemSelected(item: Item??)
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