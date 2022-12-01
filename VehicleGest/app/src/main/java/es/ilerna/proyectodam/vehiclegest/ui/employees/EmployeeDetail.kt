package es.ilerna.proyectodam.vehiclegest.ui.employees


import EmployeeFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.entities.Employee
import es.ilerna.proyectodam.vehiclegest.databinding.DetailEmployeeBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class EmployeeDetail(val data: Employee) : Fragment() {

    private var _binding: DetailEmployeeBinding? = null
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

        _binding = DetailEmployeeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.employeeDni.text = data.dni
        binding.name.text = data.name
        binding.surname.text = data.surname
        binding.phone.text = data.phone
        binding.address.text = data.address
        binding.email.text = data.email
        binding.checkadmin.isChecked = data.admin!!
        //Formatea los timestamp a fecha normal dd/mm/aa
        val simpleDateFormat = SimpleDateFormat(
            getString(R.string.dateFormat), Locale.getDefault()
        )
        val stamp = data.birthdate?.time
        val date = simpleDateFormat.format(Date(stamp!!))
        binding.birthdate.text = date.toString()

        //Foto del vehículo
        Glide.with(binding.root).load(data.photoURL).into(binding.employeeImage)

        binding.btclose.setOnClickListener {
            this.onBtClose()
        }

        return root

    }

    private fun onBtClose() {
        navBarBot.visibility = VISIBLE
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, EmployeeFragment())
        fragmentTransaction.commit()
    }


}