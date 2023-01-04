package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.customDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Alert

/**
 * Abre una ventana diálogo con los detalles de la alert
 * @param documentSnapshot Instantanea de firestore de la alerta
 */
class AlertDetail(
    private val documentSnapshot: DocumentSnapshot,
) : DetailFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var _detailAlertBinding: DetailAlertBinding? = null
    private val detailAlertBinding
        get() = _detailAlertBinding ?: throw IllegalStateException("Binding error")

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
            _detailAlertBinding = DetailAlertBinding.inflate(inflater, container, false)

            //Escuchador del boton cerrar
            detailAlertBinding.bar.btclose.setOnClickListener {
                fragmentReplacer(AlertsFragment(), parentFragmentManager)
            }

            //Escuchador del boton borrar
            detailAlertBinding.bar.btdelete.setOnClickListener {
                delDocumentSnapshot(documentSnapshot) //Borra el documento
                fragmentReplacer(AlertsFragment(), parentFragmentManager)
            }

            bindDataToForm() //Llama a la función que rellena los datos en el formulario

        } catch (exception: Exception) {
            Log.w(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace(
            )
        }
        return detailAlertBinding.root
    }


    /**
     * Rellena los datos del formulario con los datos
     */
    override fun bindDataToForm() {
        //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
        val alert: Alert? = documentSnapshot.toObject(Alert::class.java)
        detailAlertBinding.plateNumber.setText(alert?.plateNumber)
        detailAlertBinding.alertDescription.setText(alert?.description)
        detailAlertBinding.checksolved.isChecked = alert?.solved == false

        /*Formatea los timestamp a fecha normal dd/mm/aa
        Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
        El formato se puede modificar en strings.xml*/
        detailAlertBinding.date.setText(alert?.date?.let { customDateFormat(it) })

    }

    /**
     * Edita los datos del documento en firestore
     * @param documentSnapshot Instantanea de firestore del documento
     */
    override fun editDocumentSnapshot(documentSnapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    /**
     *  Al destruir el fragmento, elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _detailAlertBinding = null
    }
}