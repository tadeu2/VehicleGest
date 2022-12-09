package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.backend.DetailFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.DetailVehicleBinding

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class VehicleDetail(s: DocumentSnapshot) : DetailFragment(s) {

    private var _binding: DetailVehicleBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Enlaza al XML del formulario y lo infla
        _binding = DetailVehicleBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance().collection("vehicle");
        val root: View = binding.root

        //Escuchador del boton cerrar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(VehiclesFragment())
        }

        //Escuchador del boton cerrar
        binding.bar.btdelete.setOnClickListener {
            delDocument(s)
            fragmentReplacer(VehiclesFragment())
        }

        bindData()
        //Llama a la función que rellena los datos en el formulario
        return root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun bindData() {
        try {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val vehicle: Vehicle? = s.toObject(Vehicle::class.java)
            binding.url.setText(vehicle?.photoURL)
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
           // Vehiclegest.displayImgURL(vehicle?.photoURL.toString(), binding.vehicleImage)
            // Mostrar la barra de carga
            progressBar = ProgressBar(context)
            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(binding.vehicleImage, binding.url.text.toString(), progressBar)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun editDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}