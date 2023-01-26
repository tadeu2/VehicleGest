package es.ilerna.proyectodam.vehiclegest.ui.inventory

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItemBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param documentSnapshot Instantanea de firestore del item
 */
class ItemDetailFragment(
    documentSnapshot: DocumentSnapshot?
) : DetailFormModelFragment(documentSnapshot) {

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
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("inventory")

            //Inicializa los escuchadores de los botones
            with(getDetailItemBinding.bar) {
                btsave.visibility = View.GONE
                btedit.visibility = View.VISIBLE
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
        CoroutineScope(Dispatchers.Main).launch {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val item: Item? = documentSnapshot?.toObject(Item::class.java)
            with(getDetailItemBinding) {
                //Rellena los campos del formulario con los datos del objeto
                name.setText(item?.name)
                itemDescription.setText(item?.description)
                plateNumber.setText(item?.plateNumber)
                //Carga la foto en el formulario a partir de la URL almacenada
                if (item?.photoURL.toString().isEmpty()) {
                    itemImage.post {
                        itemImage.setImageResource(R.drawable.no_image_available)
                    }
                } else {
                    val bitmapFromUrl = Controller().getBitmapFromUrl(
                        item?.photoURL.toString()
                    ).await()
                    itemImage.post {
                        itemImage.setImageBitmap(bitmapFromUrl)
                    }
                }
            }
        }
    }

    /**
     * Metodo que rellena la entidad con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        getDetailItemBinding.apply {
            return Item(
                plateNumber.text.toString(),
                name.text.toString(),
                itemDescription.text.toString(),
                itemUrlphoto.text.toString()
            )
        }
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        getDetailItemBinding.apply {
            name.isEnabled = true
            name.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            plateNumber.isEnabled = true
            plateNumber.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            itemDescription.isEnabled = true
            itemDescription.setTextColor(
                resources.getColor(
                    R.color.md_theme_dark_errorContainer,
                    null
                )
            )
            itemUrlphoto.isEnabled = true
            itemUrlphoto.setTextColor(
                resources.getColor(
                    R.color.md_theme_dark_errorContainer,
                    null
                )
            )
        }
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