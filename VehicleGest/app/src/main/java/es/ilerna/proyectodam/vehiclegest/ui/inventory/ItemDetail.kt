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
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Item
import es.ilerna.proyectodam.vehiclegest.ui.alerts.AlertsFragment

/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param documentSnapshot Instantanea de firestore del item
 */
class ItemDetail(
    private val documentSnapshot: DocumentSnapshot
) : DetailModelFragment() {

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
                setListeners(
                    documentSnapshot,
                    parentFragmentManager,
                    InventoryFragment(),
                    btclose,
                    btdelete,
                    btsave,
                    btedit,
                )
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
        val item: Item? = documentSnapshot.toObject(Item::class.java)
        with(getDetailItemBinding) {
            //Rellena los campos del formulario con los datos del objeto
            name.setText(item?.name)
            itemDescription.setText(item?.description)
            plateNumber.setText(item?.plateNumber)
            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(itemImage, item?.photoURL.toString())
        }
    }

    /**
     * Metodo que rellena la entidad con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        TODO("Not yet implemented")
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        getDetailItemBinding.apply {
            name.isEnabled = true
            plateNumber.isEnabled = true
            itemDescription.isEnabled = true
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