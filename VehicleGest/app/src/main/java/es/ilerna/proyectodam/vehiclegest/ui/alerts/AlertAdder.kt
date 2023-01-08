package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.stringToDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Alert

/**
 * Clase que representa el fragmento de añadir alerta
 */
class AlertAdder : DetailModelFragment() {
    private var addAlertBinding: DetailAlertBinding? =
        null //Variable para enlazar el achivo de código con el XML de interfaz

    //Getter para el binding
    private val getAddAlertBinding
        get() = addAlertBinding ?: throw IllegalStateException("Binding error")

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
            //Referencia a la base de datos de Firestore
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("alert")
            //Enlaza al XML del formulario y lo infla
            addAlertBinding = DetailAlertBinding.inflate(inflater, container, false)

            makeFormEditable() //Habilita los campos para su edición

            //Inicializa los escuchadores de los botones
            with(getAddAlertBinding.bar) {
                btsave.visibility = View.VISIBLE
                btedit.visibility = View.GONE
                setListeners(
                    null,
                    parentFragmentManager,
                    AlertsFragment(),
                    DetailAlertBinding::class.java,
                    btclose,
                    btdelete,
                    btsave,
                    btedit
                )
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
                alertDescription.text.toString(),
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
            plateNumber.isFocusableInTouchMode = true
            plateNumber.isCursorVisible = true
            date.isFocusableInTouchMode = true
            alertDescription.isFocusableInTouchMode = true
            alertDescription.isCursorVisible = true
            checksolved.isFocusableInTouchMode = true
            checksolved.isClickable = true
            dateSolved.isFocusableInTouchMode = true
            alertSolution.isFocusableInTouchMode = true
            alertSolution.isCursorVisible = true

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