package es.ilerna.proyectodam.vehiclegest.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.databinding.ItemCardBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.interfaces.RecyclerAdapterListener
import es.ilerna.proyectodam.vehiclegest.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param queryFireStoreDatabase Parámetro que contiene la consulta a la base de datos
 * @param recyclerAdapterListener Parámetro que contiene el listener del adapter
 *
 */
class ItemRecyclerAdapter(
    queryFireStoreDatabase: Query,
    private val recyclerAdapterListener: RecyclerAdapterListener
) : FirestoreAdapter<ItemRecyclerAdapter.ItemViewHolder>(queryFireStoreDatabase) {
    /**
     * Clase interna
     * El holder se encarga de pintar las tarjetas
     * Implementa a RecyclerView.ViewHolder
     * @param itemCardBinding Parámetro que contiene la vista de la tarjeta
     */
    class ItemViewHolder(
        private val itemCardBinding: ItemCardBinding,
    ) : RecyclerView.ViewHolder(itemCardBinding.root) {
        /**
         * Rellena cada item de la tarjeta con los datos del objeto vehiculo
         */
        fun bindDataToCardview(
            documentSnapshot: DocumentSnapshot,
            recyclerAdapterListener: RecyclerAdapterListener
        ) {
            try {
                //Crea un hilo paralelo para que no se bloquee la UI
                CoroutineScope(Dispatchers.Main).launch {

                    with(itemCardBinding) {
                        //Inicializamos un objeto a partir de una instántanea
                        val item: Item? = documentSnapshot.toObject(Item::class.java)

                        //La asignamos a los datos del formulario
                        plateNumber.text = item?.plateNumber.toString()
                        name.text = item?.name.toString()

                        if (item?.photoURL.toString().isEmpty()) {
                            itemImage.post {
                                Controller.setDefaultImage(itemImage)
                            }
                        } else {
                            val bitmapFromUrl = Controller().getBitmapFromUrl(
                                item?.photoURL.toString()
                            ).await()
                            itemImage.post {
                                itemImage.setImageBitmap(bitmapFromUrl)
                            }
                        }

                        //Iniciamos el escuchador que accionamos al pulsar una ficha
                        itemCard.setOnClickListener {
                            recyclerAdapterListener.onItemSelected(documentSnapshot)
                        }
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
     * Crea la vista de la tarjeta y la devuelve
     * @param viewGroup Parámetro que contiene el padre
     * @param viewType Parámetro que contiene el tipo de vista
     * @return Devuelve el holder creado con la vista de la tarjeta
     */
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            //Infla la vista de la tarjeta y la devuelve
            ItemCardBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    /**
     * Función que se encarga de rellenar cada item de la tarjeta con los datos del objeto
     * @param itemViewHolder Parámetro que contiene el holder
     * @param position Parámetro que contiene la posición del objeto en la lista
     */
    override fun onBindViewHolder(
        itemViewHolder: ItemViewHolder,
        position: Int
    ) {
        //Obtiene el objeto de la lista
        getSnapshot(position)?.let { documentSnapshot ->
            //Llama a la función que rellena los datos de la tarjeta
            itemViewHolder.bindDataToCardview(documentSnapshot, recyclerAdapterListener)
        }
    }

}