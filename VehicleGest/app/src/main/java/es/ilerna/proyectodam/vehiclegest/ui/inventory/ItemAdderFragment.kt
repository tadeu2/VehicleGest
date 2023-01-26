package es.ilerna.proyectodam.vehiclegest.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItemBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.FormModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Item

/**
 * Abre una ventana diálogo con los detalles del artículo a añadir.
 */
class ItemAdderFragment : Fragment(), FormModelFragment {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var addItemBinding: DetailItemBinding? = null
    private val getAddItemBinding
        get() = addItemBinding ?: throw IllegalStateException("Binding error")

    override lateinit var dbFirestoreReference: CollectionReference //Referencia a la base de datos de Firestore

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
            btsave.visibility = View.VISIBLE
            btedit.visibility = View.GONE
            btdelete.visibility = View.GONE
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
        //Habilita los campos para su edición
        getAddItemBinding.apply {
            plateNumber.isEnabled = true
            plateNumber.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            name.isEnabled = true
            name.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            itemUrlphoto.isEnabled = true
            itemUrlphoto.setTextColor(
                resources.getColor(
                    R.color.md_theme_dark_errorContainer, null
                )
            )
            itemDescription.isEnabled = true
            itemDescription.setTextColor(
                resources.getColor(
                    R.color.md_theme_dark_errorContainer, null
                )
            )
        }
    }

}