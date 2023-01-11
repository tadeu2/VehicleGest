package es.ilerna.proyectodam.vehiclegest.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItemBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Item
import es.ilerna.proyectodam.vehiclegest.ui.alerts.AlertsFragment

/**
 * Abre una ventana diálogo con los detalles del artículo a añadir.
 */
class AddItem : DetailModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var addItemBinding: DetailItemBinding? = null
    private val getAddItemBinding
        get() = addItemBinding ?: throw IllegalStateException("Binding error")

    /**
     * Inicializa el fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        //Referencia a la base de datos de Firestore
        dbFirestoreReference = FirebaseFirestore.getInstance().collection("inventory")

        //Enlaza al XML del formulario y lo infla
        addItemBinding = DetailItemBinding.inflate(inflater, container, false)

        makeFormEditable() //Habilita los campos para su edición

        //Inicializa los escuchadores de los botones
        with(getAddItemBinding.bar) {
            btsave.visibility = android.view.View.VISIBLE
            btedit.visibility = android.view.View.GONE
            setListeners(
                null,
                parentFragmentManager,
                AlertsFragment(),
                btclose,
                btdelete,
                btsave,
                btedit
            )
        }
        //Escuchador para la imagen al cambiar la url
        with(getAddItemBinding) {
            //Carga la foto en el formulario a partir de la URL almacenada
            itemUrlphoto.doAfterTextChanged {
                //Carga la foto en el formulario a partir de la URL almacenada
                Controller().showImageFromUrl(
                    itemImage,
                    itemUrlphoto.text.toString()
                )
            }
        }

        //Llama a la función que rellena los datos en el formulario
        return getAddItemBinding.root
    }

    /**
     * Metodo que rellena la entidad con los datos del formulario
     * @return Item con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        //Rellena el objeto con los datos del formulario
        getAddItemBinding.apply {
            return {
                Item(
                    plateNumber.text.toString(),
                    name.text.toString(),
                    itemUrlphoto.text.toString(),
                    itemDescription.text.toString()
                )
            }
        }

    }

    /**
     * Metodo que rellena el formulario con los datos de la entidad
     */
    override fun bindDataToForm() {
        //No se implementa en este fragmento
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        //No se implementa en este fragmento
    }

}