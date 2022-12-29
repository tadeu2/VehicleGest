package es.ilerna.proyectodam.vehiclegest.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.DetailServiceBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.customDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Service

/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param snapshot Instantanea de firestore del servicio
 */
class ServiceDetail(val snapshot: DocumentSnapshot) :
    DetailFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var _binding: DetailServiceBinding? = null
    private val binding get() = _binding!!

    /**
     *  Fase de creación de la vista
     *  @param inflater Inflador de la vista
     *  @param container Contenedor de la vista
     *  @param savedInstanceState Instancia guardada
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        //Enlaza al XML del formulario y lo infla
        _binding = DetailServiceBinding.inflate(inflater, container, false)
        dbFirestoreReference = FirebaseFirestore.getInstance().collection("service");
        val root: View = binding.root

        //Escuchador del boton cerrar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(ServiceFragment(), parentFragmentManager)
        }

        //Escuchador del boton borrar, borrará el servicio y volverá al fragmento de servicios
        binding.bar.btdelete.setOnClickListener {
            delDocument(snapshot)
            fragmentReplacer(ServiceFragment(), parentFragmentManager)
        }

        //Llama a la función que rellena los datos en el formulario
        bindDataToForm()

        return root
    }

    /**
     * Rellena los datos del formulario con los datos del servicio
     */
    override fun bindDataToForm() {
        try {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val service: Service? = snapshot.toObject(Service::class.java)
            binding.plateNumber.setText(service?.plateNumber.toString())
            binding.costumer.setText(service?.costumer.toString())
            binding.remarks.setText(service?.remarks.toString())

            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            binding.date.setText(service?.date?.let { customDateFormat(it) })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Edita los datos del servicio
     * @param snapshot Instantanea de firestore del servicio
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