package es.ilerna.proyectodam.vehiclegest.ui.services

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailServiceBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.interfaces.FormModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Service

/**
 * Abre una ventana diálogo con los detalles del vehículo
 *
 */
class ServiceAdderFragment : Fragment(), FormModelFragment {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var addServiceBinding: DetailServiceBinding? = null
    private val getAddServiceBinding
        get() = addServiceBinding ?: throw IllegalStateException("Binding error")

    override lateinit var dbFirestoreReference: CollectionReference //Referencia a la base de datos de Firestore

    /**
     * Fase de creación de la vista del fragmento
     * @param inflater LayoutInflater para inflar la vista
     * @param container ViewGroup para la vista
     * @param savedInstanceState Bundle de datos
     * @return Vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza al XML del formulario y lo infla
            addServiceBinding = DetailServiceBinding.inflate(inflater, container, false)

            //Inicializa la base de datos
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("service")

            makeFormEditable() //Habilita los campos para su edición

            with(getAddServiceBinding.bar) {
                btsave.visibility = View.VISIBLE
                btedit.visibility = View.GONE
                btdelete.visibility = View.GONE
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e(TAG, "Error al crear la vista del fragmento", exception)
        }
        return getAddServiceBinding.root
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        with(getAddServiceBinding) {
            val views = arrayOf(plateNumber, date, costumer, remarks)
            for (view in views) {
                view.isEnabled = true
                view.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            }
        }
    }

    /**
     * Metodo que rellena el formulario con los datos de la entidad
     */
    override fun bindDataToForm() {
        //No se usa en este fragmento
    }

    /**
     * Metodo que rellena la entidad con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        getAddServiceBinding.apply {
            return Service(
                plateNumber.text.toString(),
                // Devuelve la fecha en formato dd/mm/yyyy
                Controller.stringToDateFormat(date.text.toString()),
                remarks.text.toString(),
                costumer.text.toString(
                )
            )
        }
    }

    /**
     * Elimina la vista del formulario cuando se destruye el fragmento
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        addServiceBinding = null
    }

}
