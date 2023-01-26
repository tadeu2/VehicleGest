package es.ilerna.proyectodam.vehiclegest.ui.alerts

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
import es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.stringToDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.FormModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Alert

/**
 * Clase que representa el fragmento de añadir alerta
 */
class AlertAdder : Fragment(), FormModelFragment {
    private var addAlertBinding: DetailAlertBinding? =
        null //Variable para enlazar el achivo de código con el XML de interfaz

    //Getter para el binding
    private val getAddAlertBinding
        get() = addAlertBinding ?: throw IllegalStateException("Binding error")

    override lateinit var dbFirestoreReference: CollectionReference//Referencia a la base de datos de Firestore

    /**
     * Fase de creación del fragmento
     * @param inflater Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Estado de la instancia
     */
    override fun onCreateView(
        //Variable de instancia de XML de vistas
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza al XML del formulario y lo infla
            addAlertBinding = DetailAlertBinding.inflate(inflater, container, false)

            //Referencia a la base de datos de Firestore
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("alert")
            makeFormEditable() //Habilita los campos para su edición
            with(addAlertBinding!!.bar) {
                btdelete.visibility = View.GONE //Oculta el botón de eliminar
                btedit.visibility = View.GONE //Oculta el botón de editar
                btsave.visibility = View.VISIBLE //Muestra el botón de guardar
            }

        } catch (exception: Exception) {
            Log.w(TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }

        //Llama a la función que rellena los datos en el formulario
        return getAddAlertBinding.root
    }

    /**
     * Devuelve un objeto Alerta con los datos del formulario
     * @return Objeto Alerta con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        getAddAlertBinding.apply {
            return Alert(
                plateNumber.text.toString(),
                stringToDateFormat(date.text.toString()),
                description.text.toString(),
                checksolved.isChecked,
                stringToDateFormat(dateSolved.text.toString()),
                alertSolution.text.toString()
            )
        }
    }


    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun bindDataToForm() {
        //No se implementa en este fragmento
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {

        getAddAlertBinding.apply {
            plateNumber.isEnabled = true
            plateNumber.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            date.isEnabled = true
            date.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            description.isEnabled = true
            description.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            dateSolved.isEnabled = true
            dateSolved.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            checksolved.isEnabled = true
            checksolved.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            alertSolution.isEnabled = true
            alertSolution.setTextColor(
                resources.getColor(
                    R.color.md_theme_dark_errorContainer,
                    null
                )
            )

            //Escuchador del botón de fecha
            date.setOnClickListener {
                //Abre el selector de fecha
                DatePickerFragment { day, month, year ->
                    //Muestra la fecha en el campo de texto
                    date.setText(String.format("$day/$month/$year"))
                }.show(parentFragmentManager, "datePicker")
            }

            //Escuchador del botón de fecha resuelta
            dateSolved.setOnClickListener {
                //Abre el selector de fecha
                DatePickerFragment { day, month, year ->
                    //Muestra la fecha en el campo de texto
                    dateSolved.setText(String.format("$day/$month/$year"))
                }.show(parentFragmentManager, "datePicker")
            }

        }
    }

    /**
     * Fase de destrucción del fragmento que elimina la referencia al XML de la interfaz
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        addAlertBinding = null
    }

}