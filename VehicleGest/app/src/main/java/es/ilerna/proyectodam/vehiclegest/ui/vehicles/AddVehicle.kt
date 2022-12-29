package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.databinding.AddVehicleBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.customReverseDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.AddFragment
import es.ilerna.proyectodam.vehiclegest.models.Vehicle
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class AddVehicle : AddFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var _binding: AddVehicleBinding? = null
    private val binding get() = _binding!!

    //Variable para la base de datos
    private lateinit var dbVehicle: CollectionReference

    /**
     * Fase de creación de la vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inicializa la base de datos
        dbVehicle = FirebaseFirestore.getInstance().collection("vehicle")

        //Enlaza al XML del formulario y lo infla
        _binding = AddVehicleBinding.inflate(inflater, container, false)


/*        binding.url.doAfterTextChanged {
            //Vehiclegest.displayImgURL(binding.url.text.toString(), binding.vehicleImage)
            //Vehiclegest.displayImgURL(binding.url.text.toString(), binding.itemImage)
            // Mostrar la barra de carga
            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(
                binding.vehicleImage,
                binding.url.text.toString(),
                progressBar
            )
        }*/

        /**
         * Crea un escuchador para el botón de salvar
         */
        binding.bar.btsave.setOnClickListener() {
            addDocumentToDatabase()
            fragmentReplacer(VehiclesFragment(), parentFragmentManager)
        }

        // Crea un escuchador para el botón de cancelar
        binding.bar.btclose.setOnClickListener() {
            fragmentReplacer(VehiclesFragment(), parentFragmentManager)
        }

        // Crea un escuchador para el botón de fecha
        binding.expiringItv.setOnClickListener() {
            // Crea un nuevo fragmento de diálogo de fecha
            DatePickerFragment { day, month, year ->
                // Muestra la fecha seleccionada en el campo de texto
                binding.expiringItv.setText(String.format("$day/$month/$year"))
            }
                // Muestra el diálogo
                .show(parentFragmentManager, "datePicker")
        }

        //Llama a la función que rellena los datos en el formulario
        return binding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addDocumentToDatabase() {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val plateNumber = binding.plateNumber.text.toString()
                val type = binding.type.text.toString()
                val brand = binding.brand.text.toString()
                val model = binding.model.text.toString()
                val expiryDateITV = customReverseDateFormat(binding.expiringItv.text.toString())
                val description = binding.vehicleDescription.text.toString()
                val licensed = binding.checkLicensed.isChecked
                val totalDistance = Integer.parseInt(binding.totalDistance.text.toString())
                val photoURL = binding.url.text.toString()

                val vehicle = Vehicle(
                    plateNumber, type, brand, model, expiryDateITV,
                    totalDistance, licensed, description, photoURL
                )
                dbVehicle.add(vehicle)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot escrito con ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error añadiendo documento", e)
                    }

            } catch (e: Exception) {
                Log.w(TAG, "Error en los datos", e)
            }
        }
    }

}