package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.AddVehicleBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.stringToDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Vehicle
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class AddVehicle : DetailModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var addVehicleBinding: AddVehicleBinding? = null
    private val getAddVehicleBinding get() = addVehicleBinding!!

    //Variable para la base de datos
    private lateinit var vehicleCollectionReference: CollectionReference

    /**
     * Metodo que rellena el formulario con los datos de la entidad
     */
    override fun bindDataToForm() {
        TODO("Not yet implemented")
    }

    /**
     * Metodo que rellena la entidad con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        TODO("Not yet implemented")
    }

    /**
     * Actualiza el documento en la base de datos
     */
    override fun updateDocumentToDatabase(documentSnapshot: DocumentSnapshot, any: Any) {
        TODO("Not yet implemented")
    }

    /**
     * Fase de creación de la vista del fragmento
     * @param inflater  Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Instancia guardada
     * @return Vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        try {
            //Inicializa la base de datos
            vehicleCollectionReference = FirebaseFirestore.getInstance().collection("vehicle")

            //Enlaza al XML del formulario y lo infla
            addVehicleBinding = AddVehicleBinding.inflate(inflater, container, false)

            /**
             * Crea un escuchador para el botón de salvar
             */
            getAddVehicleBinding.bar.btsave.setOnClickListener {
                addDocumentToDataBase()
                fragmentReplacer(VehiclesFragment(), parentFragmentManager)
            }

            // Crea un escuchador para el botón de cancelar
            getAddVehicleBinding.bar.btclose.setOnClickListener {
                fragmentReplacer(VehiclesFragment(), parentFragmentManager)
            }

            // Crea un escuchador para el botón de fecha
            getAddVehicleBinding.expiringItv.setOnClickListener {
                // Crea un nuevo fragmento de diálogo de fecha
                DatePickerFragment { day, month, year ->
                    // Muestra la fecha seleccionada en el campo de texto
                    getAddVehicleBinding.expiringItv.setText(String.format("$day/$month/$year"))
                }
                    // Muestra el diálogo
                    .show(parentFragmentManager, "datePicker")
            }
        } catch (exception: Exception) {
            Log.e(TAG, exception.message.toString(), exception)
        }
        //Llama a la función que rellena los datos en el formulario
        return getAddVehicleBinding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addDocumentToDataBase() {
        //Ejecuta la tarea en un hilo secundario
        Executors.newSingleThreadExecutor().execute {
            val plateNumber = getAddVehicleBinding.plateNumber.text.toString()
            val type = getAddVehicleBinding.type.text.toString()
            val brand = getAddVehicleBinding.brand.text.toString()
            val model = getAddVehicleBinding.model.text.toString()
            //Convierte la fecha a formato Date
            val expiryDateITV =
                stringToDateFormat(getAddVehicleBinding.expiringItv.text.toString())
            val description = getAddVehicleBinding.vehicleDescription.text.toString()
            val licensed = getAddVehicleBinding.checkLicensed.isChecked
            val totalDistance = Integer.parseInt(getAddVehicleBinding.totalDistance.text.toString())
            val photoURL = getAddVehicleBinding.urlphoto.text.toString()
            val vehicle = Vehicle(
                plateNumber, type, brand, model, expiryDateITV,
                totalDistance, licensed, description, photoURL
            )
            //Añade el vehículo a la base de datos
            vehicleCollectionReference.add(vehicle)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot escrito con ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error añadiendo documento", e)
                }
        }
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        TODO("Not yet implemented")
    }

}