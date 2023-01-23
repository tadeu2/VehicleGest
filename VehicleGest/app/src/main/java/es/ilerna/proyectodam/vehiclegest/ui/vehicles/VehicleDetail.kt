package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailVehicleBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.dateToStringFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.stringToDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Vehicle

/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param documentSnapshot Instantanea de firestore del vehículo
 */
class VehicleDetail(
    private val documentSnapshot: DocumentSnapshot
) : DetailModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailVehicleBinding: DetailVehicleBinding? = null
    private val getDetailVehicleBinding
        get() = detailVehicleBinding ?: throw IllegalStateException(
            "Binding error"
        ) // Lanza una excepción si el binding es nulo

    /**
     *  Fase de creación de la vista
     *  @param inflater Inflador de la vista del fragmento
     *  @param container Contenedor de la vista
     *  @param savedInstanceState Estado de la instancia
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza al XML del formulario y lo infla
            detailVehicleBinding = DetailVehicleBinding.inflate(inflater, container, false)
            // Referencia a la base de datos
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("vehicle")
            //Escuchador del boton cerrar

            with(getDetailVehicleBinding.bar) {
                setListeners(
                    documentSnapshot,
                    parentFragmentManager,
                    VehiclesFragment(),
                    btclose,
                    btdelete,
                    btsave,
                    btedit
                )
            }

            bindDataToForm() //Llama a la función que rellena los datos en el formulario

        } catch (exception: Exception) {
            Log.e(TAG, exception.message.toString(), exception)
        }

        return getDetailVehicleBinding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun bindDataToForm() {

        with(getDetailVehicleBinding) {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val vehicle: Vehicle? = documentSnapshot.toObject(Vehicle::class.java)
            urlphoto.setText(vehicle?.photoURL)
            plateNumber.setText(vehicle?.plateNumber)
            type.setText(vehicle?.type)
            brand.setText(vehicle?.brand)
            model.setText(vehicle?.model)
            vehicleDescription.setText(vehicle?.description)
            checkItvPassed.isChecked = vehicle?.itvPassed == false

            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            expiringItvDate.setText(vehicle?.expiryDateITV?.let {
                dateToStringFormat(
                    it
                )
            })

            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(
                vehicleImage,
                urlphoto.text.toString()
            )
        }

    }

    /**
     * Metodo que rellena la entidad con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        getDetailVehicleBinding.apply {
            val inputString = totalDistance.text.toString()
            val inputNumber = if(inputString.isNullOrEmpty()) 0 else inputString.toInt()
            return Vehicle(
                plateNumber.text.toString(),
                type.text.toString(),
                brand.text.toString(),
                model.text.toString(),
                stringToDateFormat(expiringItvDate.text.toString()),
                inputNumber,
                checkItvPassed.isChecked,
                vehicleDescription.text.toString(),
                urlphoto.text.toString()
            )
        }
    }


    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        getDetailVehicleBinding.apply {
            plateNumber.isEnabled = true
            plateNumber.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            type.isEnabled = true
            type.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            brand.isEnabled = true
            brand.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            model.isEnabled = true
            model.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            vehicleDescription.isEnabled = true
            vehicleDescription.setTextColor(
                resources.getColor(
                    R.color.md_theme_dark_errorContainer,
                    null
                )
            )
            checkItvPassed.isEnabled = true
            checkItvPassed.setTextColor(
                resources.getColor(
                    R.color.md_theme_dark_errorContainer,
                    null
                )
            )
            expiringItvDate.isEnabled = true
            expiringItvDate.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            totalDistance.isEnabled = true
            totalDistance.setTextColor(
                resources.getColor(
                    R.color.md_theme_dark_errorContainer,
                    null
                )
            )
            urlphoto.isEnabled = true
            urlphoto.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))

            //Escuchador del botón de fecha
            expiringItvDate.setOnClickListener {
                //Abre el selector de fecha
                DatePickerFragment { day, month, year ->
                    //Muestra la fecha en el campo de texto
                    expiringItvDate.setText(String.format("$day/$month/$year"))
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
        detailVehicleBinding = null
    }
}