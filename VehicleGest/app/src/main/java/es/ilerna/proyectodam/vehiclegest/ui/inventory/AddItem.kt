package es.ilerna.proyectodam.vehiclegest.ui.inventory

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.AddItemBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.AddFragment
import es.ilerna.proyectodam.vehiclegest.models.Item
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del artículo a añadir.
 */
class AddItem : AddFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var addItemBinding: AddItemBinding? = null
    private val getAddItemBinding
        get() = addItemBinding ?: throw IllegalStateException("Binding error")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        dbFirestoreReference = FirebaseFirestore.getInstance().collection("inventory")

        //Enlaza al XML del formulario y lo infla
        addItemBinding = AddItemBinding.inflate(inflater, container, false)

        //Carga la foto en el formulario a partir de la URL almacenada
        getAddItemBinding.url.doAfterTextChanged {
            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(
                getAddItemBinding.itemImage,
                getAddItemBinding.url.text.toString(),
            )
        }

        getAddItemBinding.bar.btsave.setOnClickListener {
            addDocumentToDatabase()
            fragmentReplacer(InventoryFragment(), parentFragmentManager)
        }

        getAddItemBinding.bar.btclose.setOnClickListener {
            fragmentReplacer(InventoryFragment(), parentFragmentManager)
        }

        //Llama a la función que rellena los datos en el formulario
        return getAddItemBinding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addDocumentToDatabase() {
        Executors.newSingleThreadExecutor().execute {
            val plateNumber = getAddItemBinding.plateNumber.text.toString()
            val name = getAddItemBinding.name.text.toString()
            val description = getAddItemBinding.itemDescription.text.toString()
            val photoURL = getAddItemBinding.url.text.toString()

            val item = Item(
                plateNumber, name, description, photoURL
            )
            dbFirestoreReference.add(item).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot escrito con ID: ${documentReference.id}")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error añadiendo documento", e)
            }
    }
}

}