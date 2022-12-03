package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.DetailFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.DetailVehicleBinding

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class VehicleDetail(uuid: ParcelUuid, val vehicle: Vehicle) : DetailFragment() {

    private var _binding: DetailVehicleBinding? = null
    private val binding get() = _binding!!
    val handler = Handler(Looper.getMainLooper())
    var image: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Esconde barras de navegación
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarTop.visibility = View.GONE
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = View.GONE

        //Enlaza al XML del formulario y lo infla
        _binding = DetailVehicleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Llama a la función que rellena los datos en el formulario
        bindData()
        return root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun bindData() {

        try {
            binding.plateNumber.setText(vehicle.plateNumber)
            binding.type.setText(vehicle?.type)
            binding.brand.setText(vehicle.brand)
            binding.model.setText(vehicle.model)
            binding.vehicleDescription.setText(vehicle?.description)
            binding.checkLicensed.isChecked = vehicle?.licensed == false

            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            binding.expiringItv.setText(vehicle?.expiryDateITV?.time?.let {
                Vehiclegest.customDateFormat(
                    it
                )
            })
            //Añade la cadena km de kilometros al final del número
            binding.totalDistance.setText(buildString {
                append(vehicle.totalDistance.toString())
                append(" KM")
            })
            //Carga la foto en el formulario a partir de la URL almacenada
            val im = java.net.URL(vehicle?.photoURL).openStream()
            image = BitmapFactory.decodeStream(im)

            handler.post {
                binding.vehicleImage.setImageBitmap(image)
            }
        }

        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun editDocument(data: Vehicle) {
        TODO("Not yet implemented")
    }

    override fun delDocument(data: Vehicle) {
/*
        db.collection("vehicle").document(data)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
*/

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}