package es.ilerna.proyectodam.vehiclegest.ui.alerts


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Alert
import es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class AlertDetail(val data: Alert) : Fragment() {

    private var _binding: DetailAlertBinding? = null
    private val binding get() = _binding!!

    private lateinit var navBarTop: MaterialToolbar
    private lateinit var navBarBot: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Esconde barras de navegación
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarTop.visibility = GONE
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = GONE


        _binding = DetailAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.plateNumber.text = data.plateNumber
        binding.alertDescription.text = data.description
        binding.checksolved.isChecked = data.solved == false

        //Formatea los timestamp a fecha normal dd/mm/aa
        //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
        //El formato se puede modificar en strings.xml
        binding.date.text = data.date?.time?.let { Vehiclegest.customDateFormat(it) }

        binding.bar.btclose.setOnClickListener {
            this.onBtClose()
        }

        return root

    }

    override fun onDestroy() {
        super.onDestroy()
        //La barra superior vuelve a ser visible al destruirse el fragmento
        navBarTop.visibility = VISIBLE
    }

    private fun onBtClose() {
        navBarBot.visibility = VISIBLE
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, AlertsFragment())
        fragmentTransaction.commit()
    }


}