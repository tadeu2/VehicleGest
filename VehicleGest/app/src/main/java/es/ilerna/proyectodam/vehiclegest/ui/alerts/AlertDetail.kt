package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.backend.DetailFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Alert
import es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class AlertDetail(val data: Alert) : DetailFragment() {

    private var _binding: DetailAlertBinding? = null
    private val binding get() = _binding!!

    /**
     *
     */
    override fun bindData() {
        binding.plateNumber.text = data.plateNumber
        binding.alertDescription.text = data.description
        binding.checksolved.isChecked = data.solved == false

        //Formatea los timestamp a fecha normal dd/mm/aa
        //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
        //El formato se puede modificar en strings.xml
        binding.date.text = data.date?.let { Vehiclegest.customDateFormat(it) }

        binding.bar.btclose.setOnClickListener {
            this.onBtClose(AlertsFragment())
        }
    }

    /**
     *
     */
    override fun editDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    /**
     *
     */
    override fun delDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //
        _binding = DetailAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root
        bindData()
        return root
    }
}