package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.AddFragment
import es.ilerna.proyectodam.vehiclegest.backend.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.AddVehicleBinding
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class AddVehicle : AddFragment() {

    private var _binding: AddVehicleBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbVehicle: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dbVehicle = FirebaseFirestore.getInstance().collection("vehicle")

        //Enlaza al XML del formulario y lo infla
        _binding = AddVehicleBinding.inflate(inflater, container, false)

        binding.url.doAfterTextChanged {
            Vehiclegest.displayImgURL(binding.url.text.toString(), binding.vehicleImage)
        }

        binding.bar.btsave.setOnClickListener() {
            addData()
            fragmentReplacer(VehiclesFragment(), parentFragmentManager)
        }

        binding.bar.btclose.setOnClickListener() {
            fragmentReplacer(VehiclesFragment(), parentFragmentManager)
        }

        binding.expiringItv.setOnClickListener() {
            val newFragment =
                DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
            newFragment.show(parentFragmentManager, "datePicker")
        }

        //Llama a la función que rellena los datos en el formulario
        return binding.root
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        binding.expiringItv.setText(String.format("$day/$month/$year"))
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addData() {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val plateNumber = binding.plateNumber.text.toString()
                val type = binding.type.text.toString()
                val brand = binding.brand.text.toString()
                val model = binding.model.text.toString()
                val expiryDateITV =
                    Vehiclegest.customReverseDateFormat(binding.expiringItv.text.toString())
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