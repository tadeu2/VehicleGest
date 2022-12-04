package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.AddFragment
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.AddVehicleBinding
import java.util.*
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class AddVehicle() : AddFragment() {

    private var _binding: AddVehicleBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: CollectionReference

    val handler = Handler(Looper.getMainLooper())
    var image: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        db = FirebaseFirestore.getInstance().collection("vehicle");

        //Enlaza al XML del formulario y lo infla
        _binding = AddVehicleBinding.inflate(inflater, container, false)

        //Llama a la función que rellena los datos en el formulario
        return binding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    fun addData() {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val plateNumber = binding.plateNumber.text.toString()
                val type = binding.type.text.toString()
                val brand = binding.brand.text.toString()
                val model = binding.model.text.toString()
                val expiryDateITV = binding.expiringItv.text.toString() as Date
                val description = binding.vehicleDescription.text.toString()
                val licensed = binding.checkLicensed.isChecked
                val totalDistance = binding.totalDistance.text.toString() as Int
                val photoURL = binding.url.text.toString()

                val vehicle: Vehicle = Vehicle(
                    plateNumber, type, brand, model, expiryDateITV,
                    totalDistance, licensed, description, photoURL
                )
                db.add(vehicle)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}