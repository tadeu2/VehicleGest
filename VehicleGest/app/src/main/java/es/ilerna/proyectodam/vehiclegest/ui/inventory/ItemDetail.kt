package es.ilerna.proyectodam.vehiclegest.ui.inventory


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.backend.DetailFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Item
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItemBinding


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class ItemDetail(s: DocumentSnapshot) : DetailFragment(s) {

    private var _binding: DetailItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Enlaza al XML del formulario y lo infla
        _binding = DetailItemBinding.inflate(inflater, container, false)
        //db = FirebaseFirestore.getInstance().collection("inventory");
        val root: View = binding.root

        //Escuchador del boton cerrar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(InventoryFragment())
        }

        //Escuchador del boton borrar
        binding.bar.btdelete.setOnClickListener {
            delDocument(s)
            fragmentReplacer(InventoryFragment())
        }

        bindData()
        //Llama a la función que rellena los datos en el formulario
        return root
    }

    override fun bindData() {
        try {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val item: Item? = s.toObject(Item::class.java)
            binding.name.setText(item?.name)
            binding.plateNumber.setText(item?.plateNumber)
            binding.itemDescription.setText(item?.description)

            //Carga la foto en el formulario a partir de la URL almacenada
            Vehiclegest.displayImgURL(item?.photoURL, binding.itemImage)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun editDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}