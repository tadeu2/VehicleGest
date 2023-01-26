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
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Vehicle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param documentSnapshot Instantanea de firestore del vehículo
 */
class VehicleDetail(
    documentSnapshot: DocumentSnapshot?
) : DetailFormModelFragment(documentSnapshot) {

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
        CoroutineScope(Dispatchers.Main).launch {
            with(getDetailVehicleBinding) {
                //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
                val vehicle: Vehicle? = documentSnapshot?.toObject(Vehicle::class.java)
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

                if (vehicle?.photoURL.toString().isEmpty()) {
                    vehicleImage.post {
                        vehicleImage.setImageResource(R.drawable.no_image_available)
                    }
                } else {
                    val bitmapFromUrl = Controller().getBitmapFromUrl(
                        vehicle?.photoURL.toString()
                    ).await()
                    vehicleImage.post {
                        vehicleImage.setImageBitmap(bitmapFromUrl)
                    }
                }
            }
        }

    }

    /**
     * Metodo que rellena la entidad con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        getDetailVehicleBinding.apply {
            val inputString = totalDistance.text.toString()
            val inputNumber = if (inputString.isNullOrEmpty()) 0 else inputString.toInt()
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
        with(getDetailVehicleBinding) {
            val views = arrayOf(
                plateNumber,
                type,
                brand,
                model,
                expiringItvDate,
                totalDistance,
                checkItvPassed,
                vehicleDescription,
                urlphoto
            )
            for (view in views) {
                view.isEnabled = true
                view.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            }
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