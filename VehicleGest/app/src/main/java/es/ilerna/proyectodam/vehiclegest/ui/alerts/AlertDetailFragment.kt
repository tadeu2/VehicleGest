package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.dateToStringFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Alert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * Abre una ventana diálogo con los detalles de la alert

 */
class AlertDetailFragment : DetailFormModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailAlertBinding: DetailAlertBinding? = null
    private val getDetailAlertBinding
        get() = detailAlertBinding ?: throw IllegalStateException("Binding error")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Crea una instancia del fragmento principal para poder volver a él
        mainFragment = AlertsFragment()
    }

    /**
     * Inicializa el fragmento
     * @param inflater Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Estado de la instancia
     * @return Vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        try {

            detailAlertBinding = DetailAlertBinding.inflate(inflater, container, false)

            //Enlaza al XML del formulario y lo infla

            //Referencia a la base de datos de Firestore
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("alert")

        } catch (exception: Exception) {
            Log.w(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace(
            )
        }
        return getDetailAlertBinding.root
    }

    /**
     * Hace editable el formulario
     */
    override fun makeFormEditable() {
        with(getDetailAlertBinding) {

            arrayOf(
                plateNumber,
                date,
                description,
                dateSolved,
                alertSolution
            ).forEach { view ->
                view.isEnabled = true
                view.setTextColor(editableEditTextColor)
            }

            checksolved.isEnabled = true

            date.setOnClickListener {
                //Abre el selector de fecha
                DatePickerFragment { day, month, year ->
                    //Muestra la fecha en el campo de texto
                    date.setText(String.format("$day/$month/$year"))
                }.show(parentFragmentManager, "datePicker")
            }
            dateSolved.setOnClickListener {
                //Abre el selector de fecha
                DatePickerFragment { day, month, year ->
                    //Muestra la fecha en el campo de texto
                    date.setText(String.format("$day/$month/$year"))
                }.show(parentFragmentManager, "datePicker")
            }
        }
    }

    /**
     * Rellena los datos del formulario con los datos
     */
    override fun bindDataToForm() {
        CoroutineScope(Dispatchers.Main).launch {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val alert: Alert? = documentSnapshot?.toObject(Alert::class.java)

            getDetailAlertBinding.apply {
                arrayOf(
                    Pair(plateNumber, alert?.plateNumber),
                    Pair(description, alert?.description),
                    Pair(alertSolution, alert?.solution),
                    Pair(date, alert?.date),
                    Pair(dateSolved, alert?.solveddate)
                ).forEach { (field, valueToFill) ->
                    if (field == date || field == dateSolved) {
                        field.setText(dateToStringFormat(valueToFill as Date?))
                    } else {
                        field.setText(valueToFill.toString())
                    }
                }

            }
        }
    }

    /**
     * Devuelve un objeto Alert con los datos del formulario
     * @return Objeto Alert
     */
    override fun fillDataFromForm(): Any {
        //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
        getDetailAlertBinding.apply {
            return Alert(
                plateNumber.text.toString(),
                Controller.stringToDateFormat(date.text.toString()),
                description.text.toString(),
                checksolved.isChecked,
                Controller.stringToDateFormat(dateSolved.text.toString()),
                alertSolution.text.toString()
            )
        }
    }

    /**
     *  Al destruir el fragmento, elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        detailAlertBinding = null
    }
}