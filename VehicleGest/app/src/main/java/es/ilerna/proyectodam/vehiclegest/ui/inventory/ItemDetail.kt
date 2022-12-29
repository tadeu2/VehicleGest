package es.ilerna.proyectodam.vehiclegest.ui.inventory


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItemBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Item
import org.checkerframework.checker.units.qual.s


/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param snapshot Instantanea de firestore del item
 */
class ItemDetail(val snapshot: DocumentSnapshot) : DetailFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var _binding: DetailItemBinding? = null
    private val binding get() = _binding!!

    //Crea la vista
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Enlaza al XML del formulario y lo infla
        _binding = DetailItemBinding.inflate(inflater, container, false)
        dbFirestoreReference = FirebaseFirestore.getInstance().collection("inventory");
        val root: View = binding.root

        //Escuchador del boton cerrar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(InventoryFragment(), parentFragmentManager)
        }

        //Escuchador del boton borrar
        binding.bar.btdelete.setOnClickListener {
            delDocument(snapshot)
            fragmentReplacer(InventoryFragment(), parentFragmentManager)
        }

        bindDataToForm()
        //Llama a la función que rellena los datos en el formulario
        return root
    }

    override fun bindDataToForm() {
        try {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val item: Item? = snapshot.toObject(Item::class.java)
            binding.name.setText(item?.name)
            binding.plateNumber.setText(item?.plateNumber)
            binding.itemDescription.setText(item?.description)

            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(binding.itemImage, item?.photoURL.toString())

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Borra el documento de la base de datos
     * @param snapshot Instantanea de firestore del item
     */
    override fun editDocument(snapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    /**
     * Al destruir la vista, elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}