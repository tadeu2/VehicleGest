package es.ilerna.proyectodam.vehiclegest.ui.services


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Service
import es.ilerna.proyectodam.vehiclegest.databinding.DetailServiceBinding


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class ServiceDetail(val data: Service) : Fragment() {

    private var _binding: DetailServiceBinding? = null
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
        navBarTop.visibility = View.GONE
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = View.GONE

        _binding = DetailServiceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.plateNumber.text = data.plateNumber.toString()
        binding.costumer.text = data.costumer.toString()
        binding.remarks.text = data.remarks.toString()

        //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
        //El formato se puede modificar en strings.xml
        binding.date.text = data?.date?.time?.let { Vehiclegest.customDateFormat(it) }

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
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, ServiceFragment())
        fragmentTransaction.commit()
    }
}