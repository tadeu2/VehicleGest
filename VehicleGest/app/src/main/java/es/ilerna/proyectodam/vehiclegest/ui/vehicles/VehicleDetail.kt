package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.DetailVehicleBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.customDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Vehicle

/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param documentSnapshot Instantanea de firestore del vehículo
 */
class VehicleDetail(
    private val documentSnapshot: DocumentSnapshot
) : DetailFragment() {

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
        try{
        //Enlaza al XML del formulario y lo infla
        detailVehicleBinding = DetailVehicleBinding.inflate(inflater, container, false)
        // Referencia a la base de datos
        dbFirestoreReference = FirebaseFirestore.getInstance().collection("vehicle")
        //Escuchador del boton cerrar
        getDetailVehicleBinding.bar.btclose.setOnClickListener {
            fragmentReplacer(VehiclesFragment(), parentFragmentManager)
        }
        //Escuchador del boton borrar, borra el vehículo y vuelve al fragmento de vehículos
        getDetailVehicleBinding.bar.btdelete.setOnClickListener {
            delDocumentSnapshot(documentSnapshot)
            fragmentReplacer(VehiclesFragment(), parentFragmentManager)
        }

        bindDataToForm() //Llama a la función que rellena los datos en el formulario
        }catch (exception:Exception){
           Log.e(TAG,exception.message.toString(), exception)
        }

        return getDetailVehicleBinding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun bindDataToForm() {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val vehicle: Vehicle? = documentSnapshot.toObject(Vehicle::class.java)
            getDetailVehicleBinding.url.setText(vehicle?.photoURL)
            getDetailVehicleBinding.plateNumber.setText(vehicle?.plateNumber)
            getDetailVehicleBinding.type.setText(vehicle?.type)
            getDetailVehicleBinding.brand.setText(vehicle?.brand)
            getDetailVehicleBinding.model.setText(vehicle?.model)
            getDetailVehicleBinding.vehicleDescription.setText(vehicle?.description)
            getDetailVehicleBinding.checkLicensed.isChecked = vehicle?.licensed == false

            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            getDetailVehicleBinding.expiringItv.setText(vehicle?.expiryDateITV?.let {
                customDateFormat(
                    it
                )
            })
            //Añade la cadena km de kilometros al final del número
            getDetailVehicleBinding.totalDistance.setText(buildString {
                append(vehicle?.totalDistance.toString())
                append(" KM")
            })
            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(
                getDetailVehicleBinding.vehicleImage,
                getDetailVehicleBinding.url.text.toString()
            )
    }

    /**
     * Metodo que rellena la entidad con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        TODO("Not yet implemented")
    }

    /**
     * Añade el documento a la base de datos
     */
    override fun addDocumentToDataBase() {
        TODO("Not yet implemented")
    }

    /**
     * Edita los datos del vehículo
     * @param documentSnapshot Instantanea de firestore del vehículo
     */
    override fun updateDocumentToDatabase(documentSnapshot: DocumentSnapshot, any: Any) {
        TODO("Not yet implemented")
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