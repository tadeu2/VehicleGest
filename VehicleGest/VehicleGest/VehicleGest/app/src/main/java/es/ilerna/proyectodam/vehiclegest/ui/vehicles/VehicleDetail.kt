package es.ilerna.proyectodam.vehiclegest.ui.vehicles


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.DialogVehicleBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class VehicleDetail(val data: Vehicle) : Fragment() {

    private var _binding: DialogVehicleBinding? = null
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

        _binding = DialogVehicleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.plateNumber.text = data.plateNumber
        binding.type.text = data.type
        binding.brand.text = data.brand
        binding.model.text = data.model
        binding.vehicleDescription.text = data.description
        binding.checkLicensed.isChecked = data.licensed == true
        //Formatea los timestamp a fecha normal dd/mm/aa
        val simpleDateFormat = SimpleDateFormat(
            getString(R.string.dateFormat), Locale.getDefault()
        )
        val stamp = data.expiryDateITV?.time
        val date = simpleDateFormat.format(Date(stamp!!))
        binding.expiringItv.text = date.toString()
        binding.totalDistance.text = buildString {
        append(data.totalDistance.toString())
        append(" KM")
    }
        binding.btclose.setOnClickListener {
            this.onBtClose()
        }

        return root

    }

    private fun onBtClose() {
        navBarBot.visibility = VISIBLE
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, VehiclesFragment())
        fragmentTransaction.commit()
    }


}