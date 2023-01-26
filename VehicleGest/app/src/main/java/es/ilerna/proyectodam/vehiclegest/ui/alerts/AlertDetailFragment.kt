package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.dateToStringFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Alert

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

            //Inicializa los escuchadores de los botones
            with(getDetailAlertBinding.bar) {
                //Escuchador del boton cerrar
                setCloseButtonListener(btclose)
                setEditButtonListener(btedit)
                setSaveButtonListener(btsave)
                setDeleteButtonListener(btdelete)
            }

            bindDataToForm() //Llama a la función que rellena los datos en el formulario

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
        val color = resources.getColor(R.color.md_theme_dark_errorContainer, null)
        with(getDetailAlertBinding) {
            plateNumber.isEnabled = false
            plateNumber.setTextColor(color)
            plateNumber.setOnClickListener(null)

            val viewsToEnable = arrayOf(
                plateNumber,
                date,
                description,
                dateSolved,
                checksolved,
                alertSolution
            )
            viewsToEnable.forEach { view ->
                view.apply {
                    isEnabled = true
                    setTextColor(color)
                    if (view == date || view == dateSolved) {
                        setOnClickListener {
                            DatePickerFragment { day, month, year ->
                                text = String.format("$day/$month/$year")
                            }.show(parentFragmentManager, "datePicker")
                        }
                    }
                }
            }
        }
    }

    /**
     * Rellena los datos del formulario con los datos
     */
    override fun bindDataToForm() {
        //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
        val alert: Alert? = documentSnapshot?.toObject(Alert::class.java)

        with(getDetailAlertBinding) {
            val views = arrayOf(
                Pair(plateNumber, alert?.plateNumber),
                Pair(description, alert?.description),
                Pair(alertSolution, alert?.solution)
            )
            views.forEach { (view, value) ->
                view.setText(value)
            }
            checksolved.isChecked = alert?.solved ?: false

            //Formatea los timestamp a fecha normal dd/mm/aa
            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            date.setText(dateToStringFormat(alert?.date))
            dateSolved.setText(dateToStringFormat(alert?.solveddate))
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