package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.backend.DetailFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.DetailVehicleBinding
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class VehicleDetail(private val s: DocumentSnapshot) : DetailFragment() {

    private var _binding: DetailVehicleBinding? = null
    private val binding get() = _binding!!

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun bindData() {
        try {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val vehicle: Vehicle? = s.toObject(Vehicle::class.java)
            binding.plateNumber.setText(vehicle?.plateNumber)
            binding.type.setText(vehicle?.type)
            binding.brand.setText(vehicle?.brand)
            binding.model.setText(vehicle?.model)
            binding.vehicleDescription.setText(vehicle?.description)
            binding.checkLicensed.isChecked = vehicle?.licensed == false

            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            binding.expiringItv.setText(vehicle?.expiryDateITV?.let {
                Vehiclegest.customDateFormat(
                    it
                )
            })
            //Añade la cadena km de kilometros al final del número
            binding.totalDistance.setText(buildString {
                append(vehicle?.totalDistance.toString())
                append(" KM")
            })
            //Carga la foto en el formulario a partir de la URL almacenada
            displayImgURL(vehicle?.photoURL, binding.vehicleImage)

            binding.bar.btdelete.setOnClickListener {
                // this.delDocument(s)
            }

            binding.bar.btclose.setOnClickListener {
                onBtClose(VehiclesFragment())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun editDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun delDocument(s: DocumentSnapshot) {
/*
        db.collection("vehicle").document(data)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
*/

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Enlaza al XML del formulario y lo infla
        _binding = DetailVehicleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        bindData()
        //Llama a la función que rellena los datos en el formulario
        return root
    }
}