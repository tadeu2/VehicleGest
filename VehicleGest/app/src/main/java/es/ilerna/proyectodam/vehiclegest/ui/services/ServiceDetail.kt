package es.ilerna.proyectodam.vehiclegest.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.DetailFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Service
import es.ilerna.proyectodam.vehiclegest.databinding.DetailServiceBinding

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class ServiceDetail(val data: Service) : DetailFragment() {

    private var _binding: DetailServiceBinding? = null
    private val binding get() = _binding!!

    override fun bindData() {
        binding.plateNumber.text = data.plateNumber.toString()
        binding.costumer.text = data.costumer.toString()
        binding.remarks.text = data.remarks.toString()

        //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
        //El formato se puede modificar en strings.xml
        binding.date.text = data.date?.let { Vehiclegest.customDateFormat(it) }

        binding.bar.btclose.setOnClickListener {
            onBtClose(ServiceFragment())
        }
    }

    override fun editDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun delDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DetailServiceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        bindData()
        return root
    }
}