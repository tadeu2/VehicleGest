package es.ilerna.proyectodam.vehiclegest.ui.services


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.entities.Service
import es.ilerna.proyectodam.vehiclegest.databinding.DetailServiceBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class ServiceDetail(val data: Service) : Fragment() {

    private var _binding: DetailServiceBinding? = null
    private val binding get() = _binding!!

    // private lateinit var navBarTop: MaterialToolbar
    private lateinit var navBarBot: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Pintar el fragment
        // navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = INVISIBLE

        _binding = DetailServiceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Formatea los timestamp a fecha normal dd/mm/aa
        val simpleDateFormat = SimpleDateFormat(
            getString(R.string.dateFormat), Locale.getDefault()
        )
        val stamp = data.date?.time
        val date = simpleDateFormat.format(Date(stamp!!))
        binding.date.text = date.toString()

        binding.btclose.setOnClickListener {
            this.onBtClose()
        }

        return root

    }

    private fun onBtClose() {
        navBarBot.visibility = VISIBLE
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, ServiceFragment())
        fragmentTransaction.commit()
    }


}