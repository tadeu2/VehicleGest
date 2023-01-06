package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.AddEmployeeBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Employee
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 * Abre una ventana para añadir un empleado
 */
class AddEmployee : DetailFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var addEmployeeBinding: AddEmployeeBinding? = null

    //Getter para el binding
    private val getAddEmployeeBinding
        get() = addEmployeeBinding ?: throw IllegalStateException("Binding error")

    /**
     * Fase de creación del fragmento
     * @param inflater Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Estado de la instancia
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza al XML del formulario y lo infla
            addEmployeeBinding = AddEmployeeBinding.inflate(inflater, container, false)
            //Referencia a la base de datos de Firebase
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("employees")

            //Carga la foto en el formulario a partir de la URL almacenada
            getAddEmployeeBinding.url.doAfterTextChanged {
                //Carga la foto en el formulario a partir de la URL almacenada
                Controller().showImageFromUrl(
                    getAddEmployeeBinding.employeeImage,
                    getAddEmployeeBinding.url.text.toString()
                )
            }

            //Escuchador del botón de añadir
            getAddEmployeeBinding.bar.btsave.setOnClickListener {
                //añade un empleado a la base de datos
                addDocumentToDataBase()
                //Vuelve a la pantalla de empleados
                fragmentReplacer(EmployeeFragment(), parentFragmentManager)
            }

            //Escuchador del botón de cerrar
            getAddEmployeeBinding.bar.btclose.setOnClickListener {
                //Vuelve a la pantalla de empleados
                fragmentReplacer(EmployeeFragment(), parentFragmentManager)
            }

            //Escuchador del botón de fecha
            getAddEmployeeBinding.birthdate.setOnClickListener {
                //Abre el selector de fecha
                DatePickerFragment { day, month, year ->
                    //Muestra la fecha en el campo de texto
                    getAddEmployeeBinding.birthdate.setText(String.format("$day/$month/$year"))
                }.show(parentFragmentManager, "datePicker")
            }
        } catch (exception: Exception) {
            Log.w(TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }

        //Llama a la función que rellena los datos en el formulario
        return getAddEmployeeBinding.root
    }



    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addDocumentToDataBase() {
        Executors.newSingleThreadExecutor().execute {
            val dni = getAddEmployeeBinding.dni.text.toString()
            val name = getAddEmployeeBinding.name.text.toString()
            val surname = getAddEmployeeBinding.surname.text.toString()
            val address = getAddEmployeeBinding.address.text.toString()
            val email = getAddEmployeeBinding.email.text.toString()
            val phone = getAddEmployeeBinding.phone.text.toString()
            //Convierte la fecha de nacimiento a Date
            val birthdate = SimpleDateFormat(
                Vehiclegest.instance.getString(R.string.dateFormat),
                Locale.getDefault()
            ).parse(getAddEmployeeBinding.birthdate.text.toString())
            val admin = getAddEmployeeBinding.checkadmin.isChecked
            val photoURL = getAddEmployeeBinding.url.text.toString()

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

        }
    }

    override fun updateDocumentToDatabase(documentSnapshot: DocumentSnapshot, any: Any) {
        TODO("Not yet implemented")
    }
    override fun bindDataToForm() {
        TODO("Not yet implemented")
    }

    override fun fillDataFromForm() {
        TODO("Not yet implemented")
    }

}