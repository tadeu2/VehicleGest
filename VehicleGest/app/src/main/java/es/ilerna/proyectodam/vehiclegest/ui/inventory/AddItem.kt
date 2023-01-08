package es.ilerna.proyectodam.vehiclegest.ui.inventory

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.AddItemBinding
import es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItemBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Item
import es.ilerna.proyectodam.vehiclegest.ui.alerts.AlertsFragment
import java.util.concurrent.Executors

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

        with(getAddItemBinding){}
        //Carga la foto en el formulario a partir de la URL almacenada
        getAddItemBinding.urlphoto.doAfterTextChanged {
            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(
                getAddItemBinding.itemImage,
                getAddItemBinding.urlphoto.text.toString(),
            )
        }

        makeFormEditable() //Habilita los campos para su edición

        //Inicializa los escuchadores de los botones
        with(getAddAlertBinding.bar) {
            btsave.visibility = android.view.View.VISIBLE
            btedit.visibility = android.view.View.GONE
            setListeners(
                null,
                parentFragmentManager,
                AlertsFragment(),
                es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding::class.java,
                btclose,
                btdelete,
                btsave,
                btedit
            )
        }

        //Llama a la función que rellena los datos en el formulario
        return getAddItemBinding.root
    }

    /**
     * Metodo que rellena el formulario con los datos de la entidad
     */
    override fun bindDataToForm() {
        TODO("Not yet implemented")
    }

    /**
     * Metodo que rellena la entidad con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        TODO("Not yet implemented")
    }


    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addDocumentToDataBase() {
        Executors.newSingleThreadExecutor().execute {
            val plateNumber = getAddItemBinding.plateNumber.text.toString()
            val name = getAddItemBinding.name.text.toString()
            val description = getAddItemBinding.itemDescription.text.toString()
            val photoURL = getAddItemBinding.urlphoto.text.toString()

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

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        TODO("Not yet implemented")
    }

}