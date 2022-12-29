package es.ilerna.proyectodam.vehiclegest.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.databinding.ItemCardBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper
import es.ilerna.proyectodam.vehiclegest.interfaces.ModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Item
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param query Parámetro que contiene la consulta a la base de datos
 * @param listener Parámetro que contiene el listener del adapter
 */
class ItemRecyclerAdapter(
    query: Query,
    private val listener: DataHelper.AdapterListener
) : FirestoreAdapter<ItemRecyclerAdapter.ItemViewHolder>(query) {

    /**
     * Clase interna
     * El holder se encarga de pintar las tarjetas
     * Implementa a RecyclerView.ViewHolder
     * @param binding Parámetro que contiene la vista de la tarjeta
     */
    class ItemViewHolder(
        private val binding: ItemCardBinding,

        ) : ViewHolder(binding.root) {

        /**
         * Rellena cada item de la tarjeta con los datos del objeto vehiculo
         */
        fun bindDataToCardview(
            snapshot: DocumentSnapshot,
            listener: DataHelper.AdapterListener
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

                    //Carga la foto en el formulario a partir de la URL almacenada
                    Controller().showImageFromUrl(
                        binding.itemImage,
                        item?.photoURL.toString(),
                    )

                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    binding.itemCard.setOnClickListener {
                        listener.onItemSelected(snapshot)
                    }
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString(), e)
                e.printStackTrace()
            } catch (e2: NullPointerException) {
                Log.e("Error", "Referencia nula", e2)
                e2.printStackTrace()
            }
        }
    }


    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface ItemAdapterListener {
        //Función que se ejecutará al hacer click en una ficha
        fun onItemSelected(snapshot: DocumentSnapshot?)

        //Función que se ejecutará al hacer click en el botón de añadir
        fun onAddItemButtonClick()
    }

    /**
     * Crea el holder
     * @param parent Parámetro que contiene el padre
     * @param viewType Parámetro que contiene el tipo de vista
     * @return Devuelve el holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Función que se encarga de rellenar cada item de la tarjeta con los datos del objeto
     * @param holder Parámetro que contiene el holder
     * @param position Parámetro que contiene la posición del objeto en la lista
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //Obtiene el objeto de la lista
        getSnapshot(position)?.let { snapshot ->
            //Llama a la función que rellena los datos de la tarjeta
            holder.bindDataToCardview(snapshot, listener)
        }
    }

}