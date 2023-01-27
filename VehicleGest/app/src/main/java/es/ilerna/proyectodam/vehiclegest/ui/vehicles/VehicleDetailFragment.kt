package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
 */
class VehicleDetailFragment : DetailFormModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailVehicleBinding: DetailVehicleBinding? = null
    private val getDetailVehicleBinding
        get() = detailVehicleBinding ?: throw IllegalStateException(
            "Binding error"
        ) // Lanza una excepción si el binding es nulo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Crea una instancia del fragmento principal para poder volver a él
        mainFragment = VehiclesFragment()
    }

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

            //Obtiene la instantanea de la base de datos y la convierte en un objeto
            val vehicle: Vehicle? = documentSnapshot?.toObject(Vehicle::class.java)

            getDetailVehicleBinding.apply {
                val formFieldsToFill = arrayOf(
                    Pair(plateNumber, vehicle?.plateNumber),
                    Pair(type, vehicle?.type),
                    Pair(brand, vehicle?.brand),
                    Pair(model, vehicle?.model),
                    Pair(totalDistance, vehicle?.totalDistance),
                    Pair(vehicleDescription, vehicle?.description),
                    Pair(urlphoto, vehicle?.photoURL)
                )

                formFieldsToFill.forEach { (field, valueToFill) ->
                    field.setText(valueToFill.toString())
                }

                checkItvPassed.isChecked = vehicle?.itvPassed == false

                //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
                //El formato se puede modificar en strings.xml
                expiringItvDate.setText(dateToStringFormat(vehicle?.expiryDateITV))

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
            val inputStringDistance = totalDistance.text.toString()
            val totalDistance =
                if (inputStringDistance.isEmpty()) 0 else inputStringDistance.toInt()
            return Vehicle(
                plateNumber.text.toString(),
                type.text.toString(),
                brand.text.toString(),
                model.text.toString(),
                stringToDateFormat(expiringItvDate.text.toString()),
                totalDistance,
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