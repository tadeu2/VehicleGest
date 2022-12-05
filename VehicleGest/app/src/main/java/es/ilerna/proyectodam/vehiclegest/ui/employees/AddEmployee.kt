package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest.Companion.customReverseDateFormat
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.data.entities.Employee
import es.ilerna.proyectodam.vehiclegest.databinding.AddEmployeeBinding
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class AddEmployee : Fragment() {

    private var _binding: AddEmployeeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbEmployee: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dbEmployee = FirebaseFirestore.getInstance().collection("employees");

        //Enlaza al XML del formulario y lo infla
        _binding = AddEmployeeBinding.inflate(inflater, container, false)

        binding.url.doOnTextChanged { text, start, count, after ->
            //Carga la foto en el formulario a partir de la URL almacenada
            Vehiclegest.displayImgURL(binding.url.text.toString(), binding.employeeImage)
        }

        binding.bar.btsave.setOnClickListener() {
            addData()
            fragmentReplacer(EmployeeFragment(), parentFragmentManager)
        }

        binding.bar.btclose.setOnClickListener() {
            fragmentReplacer(EmployeeFragment(), parentFragmentManager)
        }

        binding.birthdate.setOnClickListener() {
            val newFragment =
                DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
            newFragment.show(parentFragmentManager, "datePicker")
        }

        //Llama a la función que rellena los datos en el formulario
        return binding.root
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        binding.birthdate.setText(String.format("$day/$month/$year"))
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    private fun addData() {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val dni = binding.dni.text.toString()
                val name = binding.name.text.toString()
                val surname = binding.surname.text.toString()
                val address = binding.address.text.toString()
                val email = binding.email.text.toString()
                val phone = binding.phone.text.toString()
                val birthdate =
                    customReverseDateFormat(binding.birthdate.text.toString())
                val admin = binding.checkadmin.isChecked
                val photoURL = binding.url.text.toString()

                val employee = Employee(
                    dni, name, surname, address, email, phone, birthdate, photoURL, admin
                )
                dbEmployee.add(employee)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot escrito con ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error añadiendo documento", e)
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}