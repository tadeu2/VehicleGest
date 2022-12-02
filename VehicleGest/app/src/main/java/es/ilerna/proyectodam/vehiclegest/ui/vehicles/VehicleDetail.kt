package es.ilerna.proyectodam.vehiclegest.ui.vehicles


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.DetailVehicleBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class VehicleDetail(val data: Vehicle) : Fragment() {

    private var _binding: DetailVehicleBinding? = null
    private val binding get() = _binding!!

    private lateinit var navBarTop: NavigationBarView
    private lateinit var navBarBot: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        //Pintar el fragment
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarTop.visibility= GONE
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = GONE

        _binding = DetailVehicleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        bindData()

        return root
    }

    private fun bindData() {

        binding.plateNumber.text = data.plateNumber
        binding.type.text = data.type
        binding.brand.text = data.brand
        binding.model.text = data.model
        binding.vehicleDescription.text = data.description
        binding.checkLicensed.isChecked = data.licensed == false

        //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
        //El formato se puede modificar en strings.xml
        binding.expiringItv.text =
            data.expiryDateITV?.time?.let { Vehiclegest.customDateFormat(it) }
        Glide.with(binding.root).load(data.photoURL).into(binding.vehicleImage)
        //Añade la cadena km de kilometros al final del número
        binding.totalDistance.text = buildString {
            append(data.totalDistance.toString())
            append(" KM")
            //Foto del vehículo
        }
        binding.bar.btclose.setOnClickListener {
            this.onBtClose()
        }

    }

    private fun onBtEdit() {}

    private fun onBtClose() {
        navBarBot.visibility = VISIBLE
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, VehiclesFragment())
        fragmentTransaction.commit()
    }


}