package es.ilerna.proyectodam.vehiclegest.ui.inspections

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
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItvBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.stringToDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.FormModelFragment
import es.ilerna.proyectodam.vehiclegest.models.ITV

/**
 * Clase que representa el fragmento de añadir ITV
 */
class ItvAdderFragment : Fragment(), FormModelFragment {

    //Enlace al xml de la interfaz
    private var addItvBinding: DetailItvBinding? = null

    //Getter para el binding
    private val getAddItvBinding
        get() = addItvBinding ?: throw IllegalStateException("Binding error")

    override lateinit var dbFirestoreReference: CollectionReference //Referencia a la base de datos de Firestore

    /**
     * Fase de creación del fragmento
     * @param inflater Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Estado de la instancia
     * @return Vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        try {
            //Referencia a la base de datos de Firestore
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("ITV")

            //Enlaza al XML del formulario y lo infla
            addItvBinding = DetailItvBinding.inflate(inflater, container, false)

            makeFormEditable() //Habilita los campos para su edición

            //Añade los listeners a los botones
            with(getAddItvBinding.bar) {
                btdelete.visibility = View.GONE //Oculta el botón de eliminar
                btedit.visibility = View.GONE //Oculta el botón de editar
                btsave.visibility = View.VISIBLE //Muestra el botón de guardar
            }

        } catch (exception: Exception) {
            Log.e(TAG, exception.message.toString(), exception)
        }
        //Llama a la función que rellena los datos en el formulario
        return getAddItvBinding.root
    }

    /**
     * Función que pasa los datos al formulario
     */
    override fun bindDataToForm() {
        //No se implementa en este fragmento
    }

    /**
     * Funcion que rellema los datos desde el formulario
     */
    override fun fillDataFromForm(): Any {
        getAddItvBinding.apply {
            //Rellena los datos del formulario
            return ITV(
                stringToDateFormat(date.text.toString()),
                remarks.text.toString()
            )
        }
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        with(getAddItvBinding) {
            //Hace editable el formulario
            date.isEnabled = true
            date.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            remarks.isEnabled = true
            remarks.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))

            //Escuchador del botón de fecha
            date.setOnClickListener {
                //Abre el selector de fecha
                DatePickerFragment { day, month, year ->
                    //Muestra la fecha en el campo de texto
                    date.setText(String.format("$day/$month/$year"))
                }.show(parentFragmentManager, "datePicker")
            }
        }
    }

    /**
     * Fase de destrucción del fragmento y eliminación de la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        addItvBinding = null
    }
}