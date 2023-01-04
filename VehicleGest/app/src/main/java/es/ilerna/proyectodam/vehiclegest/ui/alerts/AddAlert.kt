package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.AddAlertBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.customReverseDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.AddFragment
import es.ilerna.proyectodam.vehiclegest.models.Alert
import java.util.concurrent.Executors

/**
 * Clase que representa el fragmento de añadir alerta
 */
class AddAlert : AddFragment() {
    private var _addAlertBinding: AddAlertBinding? =
        null //Variable para enlazar el achivo de código con el XML de interfaz
    //Getter para el binding
    private val getAddAlertBinding
        get() = _addAlertBinding ?: throw IllegalStateException("Binding error")


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
            _addAlertBinding = AddAlertBinding.inflate(inflater, container, false)

            //Escuchador del botón de añadir
            getAddAlertBinding.bar.btsave.setOnClickListener {
                addDocumentToDatabase()
                fragmentReplacer(AlertsFragment(), parentFragmentManager)
            }

            //Escuchador del botón de cancelar
            getAddAlertBinding.bar.btclose.setOnClickListener {
                fragmentReplacer(AlertsFragment(), parentFragmentManager)
            }

            //Escuchador del botón de fecha
            getAddAlertBinding.date.setOnClickListener {
                DatePickerFragment { day, month, year -> String.format("$day/$month/$year") }
                    .show(parentFragmentManager, "datePicker")
            }
        } catch (exception: Exception) {
            Log.w(TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }

        //Llama a la función que rellena los datos en el formulario
        return getAddAlertBinding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addDocumentToDatabase() {
        Executors.newSingleThreadExecutor().execute {
            val plateNumber = getAddAlertBinding.plateNumber.text.toString()
            val date = customReverseDateFormat(getAddAlertBinding.date.text.toString())
            val description = getAddAlertBinding.alertDescription.text.toString()
            val solved = getAddAlertBinding.checksolved.isChecked
            val alert = Alert(
                plateNumber, date, description, solved
            )
            dbFirestoreReference.add(alert)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot escrito con ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error añadiendo documento", e)
                }
        }
    }

    /**
     * Fase de destrucción del fragmento que elimina la referencia al XML de la interfaz
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _addAlertBinding = null
    }

}