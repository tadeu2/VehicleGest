package es.ilerna.proyectodam.vehiclegest.ui.inventory

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.databinding.AddItemBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.AddFragment
import es.ilerna.proyectodam.vehiclegest.models.Item
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class AddItem : AddFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var _binding: AddItemBinding? = null
    private val binding get() = _binding!!
    //Variable para la base de datos
    private lateinit var dbitem: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        dbitem = FirebaseFirestore.getInstance().collection("inventory");

        //Enlaza al XML del formulario y lo infla
        _binding = AddItemBinding.inflate(inflater, container, false)

        //Carga la foto en el formulario a partir de la URL almacenada
        binding.url.doAfterTextChanged {
            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(
                binding.itemImage,
                binding.url.text.toString(),
            )
        }

        binding.bar.btsave.setOnClickListener() {
            addDocumentToDatabase()
            fragmentReplacer(InventoryFragment(), parentFragmentManager)
        }

        binding.bar.btclose.setOnClickListener() {
            fragmentReplacer(InventoryFragment(), parentFragmentManager)
        }

        //Llama a la función que rellena los datos en el formulario
        return binding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addDocumentToDatabase() {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val plateNumber = binding.plateNumber.text.toString()
                val name = binding.name.text.toString()
                val description = binding.itemDescription.text.toString()
                val photoURL = binding.url.text.toString()

                val item = Item(
                    plateNumber, name, description, photoURL
                )
                dbitem.add(item).addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot escrito con ID: ${documentReference.id}")
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error añadiendo documento", e)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}