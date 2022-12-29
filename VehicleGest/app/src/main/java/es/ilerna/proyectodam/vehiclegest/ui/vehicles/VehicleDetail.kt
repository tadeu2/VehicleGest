package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.databinding.DetailVehicleBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.customDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Vehicle
import org.checkerframework.checker.units.qual.s

/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param snapshot Instantanea de firestore del vehículo
 */
class VehicleDetail(val snapshot: DocumentSnapshot) : DetailFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var _binding: DetailVehicleBinding? = null
    private val binding get() = _binding!!

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
        //Enlaza al XML del formulario y lo infla
        _binding = DetailVehicleBinding.inflate(inflater, container, false)
        dbFirestoreReference = FirebaseFirestore.getInstance().collection("vehicle");
        val root: View = binding.root

        //Escuchador del boton cerrar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(VehiclesFragment(), parentFragmentManager)
        }

        //Escuchador del boton cerrar, borra el vehículo y vuelve al fragmento de vehículos
        binding.bar.btdelete.setOnClickListener {
            delDocument(snapshot)
            fragmentReplacer(VehiclesFragment(), parentFragmentManager)
        }

        bindDataToForm()
        //Llama a la función que rellena los datos en el formulario
        return root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun bindDataToForm() {
        try {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val vehicle: Vehicle? = snapshot.toObject(Vehicle::class.java)
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
                customDateFormat(
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
            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(
                binding.vehicleImage,
                binding.url.text.toString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Edita los datos del vehículo
     * @param snapshot Instantanea de firestore del vehículo
     */
    override fun editDocument(snapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    /**
     * Al destruir la vista, elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}