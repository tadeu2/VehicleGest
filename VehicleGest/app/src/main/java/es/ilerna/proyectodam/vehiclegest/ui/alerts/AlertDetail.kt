package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.dateToStringFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Alert

/**
 * Abre una ventana diálogo con los detalles de la alert
 * @param documentSnapshot Instantanea de firestore de la alerta
 */
class AlertDetail(
    private val documentSnapshot: DocumentSnapshot,
) : DetailModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailAlertBinding: DetailAlertBinding? = null
    private val getDetailAlertBinding
        get() = detailAlertBinding ?: throw IllegalStateException("Binding error")

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
            //Enlaza al XML del formulario y lo infla
            detailAlertBinding = DetailAlertBinding.inflate(inflater, container, false)

            //Referencia a la base de datos de Firestore
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("alert")

            //Inicializa los escuchadores de los botones
            with(getDetailAlertBinding.bar) {
                btsave.visibility = View.GONE
                btedit.visibility = View.VISIBLE
                setListeners(
                    documentSnapshot,
                    parentFragmentManager,
                    AlertsFragment(),
                    btclose,
                    btdelete,
                    btsave,
                    btedit
                )
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
        getDetailAlertBinding.apply {
            /* for (i in 0 until root.childCount) {
                 root.getChildAt(i).isEnabled = true
                 root.getChildAt(i).setTextColor(resources.getColor(R.color.ic_launcher_background, null))}*/
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
            alertSolution.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))

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
 * Rellena los datos del formulario con los datos
 */
override fun bindDataToForm() {
    //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
    val alert: Alert? = documentSnapshot.toObject(Alert::class.java)

    with(getDetailAlertBinding) {
        plateNumber.setText(alert?.plateNumber)
        description.setText(alert?.description)
        checksolved.isChecked = alert?.solved!!
        alertSolution.setText(alert.solution)

        //Formatea los timestamp a fecha normal dd/mm/aa
        //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
        //El formato se puede modificar en strings.xml
        date.setText(dateToStringFormat(alert.date))
        dateSolved.setText(dateToStringFormat(alert.solveddate))
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