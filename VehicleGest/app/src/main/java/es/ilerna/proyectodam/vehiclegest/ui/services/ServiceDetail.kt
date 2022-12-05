package es.ilerna.proyectodam.vehiclegest.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.DetailFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Service
import es.ilerna.proyectodam.vehiclegest.databinding.DetailServiceBinding
import es.ilerna.proyectodam.vehiclegest.ui.vehicles.VehiclesFragment

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class ServiceDetail(s: DocumentSnapshot) : DetailFragment(s) {

    private var _binding: DetailServiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Enlaza al XML del formulario y lo infla
        _binding = DetailServiceBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance().collection("service");
        val root: View = binding.root

        //Escuchador del boton cerrar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(ServiceFragment())
        }

        //Escuchador del boton borrar
        binding.bar.btdelete.setOnClickListener {
            delDocument(s)
            fragmentReplacer(ServiceFragment())
        }

        //Llama a la función que rellena los datos en el formulario
        bindData()

        return root
    }

    override fun bindData() {
        try {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val service: Service? = s.toObject(Service::class.java)
            binding.plateNumber.setText(service?.plateNumber.toString())
            binding.costumer.setText(service?.costumer.toString())
            binding.remarks.setText(service?.remarks.toString())

            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            binding.date.setText(service?.date?.let { Vehiclegest.customDateFormat(it) })

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