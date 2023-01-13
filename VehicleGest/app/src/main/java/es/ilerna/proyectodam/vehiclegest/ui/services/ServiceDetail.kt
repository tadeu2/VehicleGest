package es.ilerna.proyectodam.vehiclegest.ui.services

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailServiceBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.dateToStringFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Service

/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param documentSnapshot Instantanea de firestore del servicio
 */
class ServiceDetail(
    private val documentSnapshot: DocumentSnapshot
) : DetailModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailServiceBinding: DetailServiceBinding? = null
    private val getDetailServiceBinding
        get() = detailServiceBinding ?: throw IllegalStateException(
            "Binding error"
        )  // Si no se enlaza el XML con el código, lanza una excepción

    /**
     *  Fase de creación de la vista
     *  @param inflater Inflador de la vista
     *  @param container Contenedor de la vista
     *  @param savedInstanceState Instancia guardada
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza al XML del formulario y lo infla
            detailServiceBinding = DetailServiceBinding.inflate(inflater, container, false)

            //Inicializa los escuchadores de los botones
            with(getDetailServiceBinding.bar) {
                btsave.visibility = View.GONE
                btedit.visibility = View.VISIBLE
                setListeners(
                    documentSnapshot,
                    parentFragmentManager,
                    ServiceFragment(),
                    btclose,
                    btdelete,
                    btsave,
                    btedit
                )
            }

            //Llama a la función que rellena los datos en el formulario
            bindDataToForm()
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
        }
        return getDetailServiceBinding.root
    }

    /**
     * Rellena los datos del formulario con los datos del servicio
     */
    override fun bindDataToForm() {
        with(getDetailServiceBinding) {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val service: Service? = documentSnapshot.toObject(Service::class.java)
            plateNumber.setText(service?.plateNumber.toString())
            costumer.setText(service?.costumer.toString())
            remarks.setText(service?.remarks.toString())

            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            date.setText(service?.date?.let { dateToStringFormat(it) })
        }
    }

    /**
     * Metodo que rellena la entidad con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        getDetailServiceBinding.apply {
            return Service(
                plateNumber.text.toString(),
                Controller.stringToDateFormat(date.text.toString()),
                costumer.text.toString(),
                remarks.text.toString()
            )
        }
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        getDetailServiceBinding.apply {
            plateNumber.isEnabled = true
            plateNumber.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            costumer.isEnabled = true
            costumer.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            remarks.isEnabled = true
            remarks.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            date.isEnabled = true
            date.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))

            //Escuchador del botón de fecha
            date.setOnClickListener {
                //ºAbre el selector de fecha
                DatePickerFragment { day, month, year ->
                    //Muestra la fecha en el campo de texto
                    date.setText(String.format("$day/$month/$year"))
                }.show(parentFragmentManager, "datePicker")
            }
        }
    }

    /**
     * Al destruir la vista, elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        detailServiceBinding = null
    }

}