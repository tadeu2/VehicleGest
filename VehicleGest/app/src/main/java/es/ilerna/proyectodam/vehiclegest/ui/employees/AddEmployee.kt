package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.backend.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.AddEmployeeBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.AddFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Employee
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class AddEmployee : DetailFragment() {

    //Inicializamos el binding
    private var _binding: AddEmployeeBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding error")

    //Inicializamos el controlador
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Enlaza al XML del formulario y lo infla
        _binding = AddEmployeeBinding.inflate(inflater, container, false)
        //Referencia a la base de datos de Firebase
        dbFirestoreReference = FirebaseFirestore.getInstance().collection("employees")

        //Carga la foto en el formulario a partir de la URL almacenada
        binding.url.doAfterTextChanged {
            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(
                binding.employeeImage,
                binding.url.text.toString()
            )
        }

        //Escuchador del botón de añadir
        binding.bar.btsave.setOnClickListener {
            //añade un empleado a la base de datos
            addData()
            //Vuelve a la pantalla de empleados
            fragmentReplacer(EmployeeFragment(), parentFragmentManager)
        }

        //Escuchador del botón de cerrar
        binding.bar.btclose.setOnClickListener {
            //Vuelve a la pantalla de empleados
            fragmentReplacer(EmployeeFragment(), parentFragmentManager)
        }

        //Escuchador del botón de fecha
        binding.birthdate.setOnClickListener {
            //Abre el selector de fecha
            DatePickerFragment { day, month, year ->
                //Muestra la fecha en el campo de texto
                binding.birthdate.setText(String.format("$day/$month/$year"))
            }.show(parentFragmentManager, "datePicker")
        }

        //Llama a la función que rellena los datos en el formulario
        return binding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addData() {
        Executors.newSingleThreadExecutor().execute {
            try {
                val dni = binding.dni.text.toString()
                val name = binding.name.text.toString()
                val surname = binding.surname.text.toString()
                val address = binding.address.text.toString()
                val email = binding.email.text.toString()
                val phone = binding.phone.text.toString()
                //Convierte la fecha de nacimiento a Date
                val birthdate = SimpleDateFormat(
                        Vehiclegest.instance.getString(R.string.dateFormat),
                        Locale.getDefault()
                    ).parse(binding.birthdate.text.toString())
                val admin = binding.checkadmin.isChecked
                val photoURL = binding.url.text.toString()

                //Crea un nuevo empleado
                val employee = Employee(
                    dni, name, surname, address, email, phone, birthdate, photoURL, admin
                )
                //Añade el empleado a la base de datos
                dbFirestoreReference.add(employee)
                    //Si se ha añadido correctamente
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot escrito con ID: ${documentReference.id}")
                    }
                    //Si no se ha añadido correctamente
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error añadiendo documento", e)
                    }

            } catch (e: Exception) {
                Log.w(TAG, "Error en los datos", e)
                e.printStackTrace()
            } catch (e2: NullPointerException) {
                Log.w(TAG, "Error en los datos", e2)
                e2.printStackTrace()
            }
        }
    }

}