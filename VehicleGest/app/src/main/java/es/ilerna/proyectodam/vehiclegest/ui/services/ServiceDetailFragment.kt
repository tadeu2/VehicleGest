package es.ilerna.proyectodam.vehiclegest.ui.services

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.DetailServiceBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.dateToStringFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Service

/**
 * Abre una ventana diálogo con los detalles del servicio
 */
class ServiceDetailFragment : DetailFormModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailServiceBinding: DetailServiceBinding? = null
    private val getDetailServiceBinding
        get() = detailServiceBinding ?: throw IllegalStateException(
            "Binding error"
        )  // Si no se enlaza el XML con el código, lanza una excepción

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Crea una instancia del fragmento principal para poder volver a él
        mainFragment = ServiceFragment()
    }

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

            //Referencia a la base de datos de Firebase
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("employees")

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
            val service: Service? = documentSnapshot?.toObject(Service::class.java)

            val views = arrayOf(
                Pair(plateNumber, service?.plateNumber),
                Pair(costumer, service?.costumer),
                Pair(remarks, service?.remarks)
            )

            views.forEach { (view, value) ->
                view.setText(value)
            }

            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            date.setText(dateToStringFormat(service?.date))
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
            //Crea una lista de los campos de texto y los pinta de rojo
            val viewListToEnable = arrayOf(plateNumber, costumer, remarks, date)
            viewListToEnable.forEach { view ->
                view.isEnabled = true
                view.setTextColor(editableEditTextColor)
            }
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