package es.ilerna.proyectodam.vehiclegest.ui.services

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailServiceBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Service

/**
 * Abre una ventana diálogo con los detalles del vehículo
 *
 */
class ServiceAdder : DetailModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var addServiceBinding: DetailServiceBinding? = null
    private val getAddServiceBinding
        get() = addServiceBinding ?: throw IllegalStateException("Binding error")

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
                setListeners(
                    null,
                    parentFragmentManager,
                    ServiceFragment(),
                    btclose,
                    btdelete,
                    btsave,
                    btedit
                )
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
        with(getAddServiceBinding){
            plateNumber.isEnabled = true
            plateNumber.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            date.isEnabled = true
            date.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            costumer.isEnabled = true
            costumer.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            date.isEnabled = true
            date.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            remarks.isEnabled = true
            remarks.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))    

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
