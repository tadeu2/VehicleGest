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
class AlertDetail(s: DocumentSnapshot) : DetailFragment(s) {

    private var _binding: DetailAlertBinding? = null
    private val binding get() = _binding!!

    /**
     *
     */
    override fun bindData() {

        try {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val alert: Alert? = s.toObject(Alert::class.java)
            binding.plateNumber.setText(alert?.plateNumber)
            binding.alertDescription.setText(alert?.description)
            binding.checksolved.isChecked = alert?.solved == false

            //Formatea los timestamp a fecha normal dd/mm/aa
            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            binding.date.setText(alert?.date?.let { Vehiclegest.customDateFormat(it) })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *
     */
    override fun editDocument(s: DocumentSnapshot) {
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

        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(AlertsFragment())
        }
        return root
    }
}