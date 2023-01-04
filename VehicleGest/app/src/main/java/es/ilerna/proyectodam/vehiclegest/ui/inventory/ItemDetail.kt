package es.ilerna.proyectodam.vehiclegest.ui.inventory

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItemBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Item

/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param snapshot Instantanea de firestore del item
 */
class ItemDetail(
    val snapshot: DocumentSnapshot
) : DetailFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailItemBinding: DetailItemBinding? = null
    private val getDetailItemBinding
        get() = detailItemBinding
            ?: throw IllegalStateException("Binding error") //Si no se puede enlazar, lanza una excepción

    /**
     * Fase de creación de la vista
     * @param inflater  Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Instancia guardada
     * @return Vista de la interfaz
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza al XML del formulario y lo infla
            detailItemBinding = DetailItemBinding.inflate(inflater, container, false)
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("inventory");
            val root: View = getDetailItemBinding.root

            //Escuchador del boton cerrar
            getDetailItemBinding.bar.btclose.setOnClickListener {
                fragmentReplacer(InventoryFragment(), parentFragmentManager)
            }

            //Escuchador del boton borrar
            getDetailItemBinding.bar.btdelete.setOnClickListener {
                delDocumentSnapshot(snapshot)
                fragmentReplacer(InventoryFragment(), parentFragmentManager)
            }

            bindDataToForm()//Llama a la función que rellena los datos en el formulario
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
        return getDetailItemBinding.root
    }

    /**
     * Función que rellena los datos del formulario
     */
    override fun bindDataToForm() {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val item: Item? = snapshot.toObject(Item::class.java)
            getDetailItemBinding.name.setText(item?.name)
            getDetailItemBinding.plateNumber.setText(item?.plateNumber)
            getDetailItemBinding.itemDescription.setText(item?.description)

            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(getDetailItemBinding.itemImage, item?.photoURL.toString())
    }

    /**
     * Borra el documento de la base de datos
     * @param documentSnapshot Instantanea de firestore del item
     */
    override fun editDocumentSnapshot(documentSnapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    /**
     * Al destruir la vista, elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        detailItemBinding = null
    }
}