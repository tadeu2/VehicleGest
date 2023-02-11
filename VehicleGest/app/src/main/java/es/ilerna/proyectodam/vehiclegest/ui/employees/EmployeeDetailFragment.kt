package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailEmployeeBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.dateToStringFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Employee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Abre una ventana diálogo con los detalles del empleado
 */
class EmployeeDetailFragment : DetailFormModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailEmployeeBinding: DetailEmployeeBinding? = null
    private val getDetailEmployeeBinding
        get() = detailEmployeeBinding ?: throw IllegalStateException("Binding error")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inicializa el fragmento principal para poder volver
        mainFragment = EmployeeFragment()
    }

    /**
     * Fase de creación de la vista
     * @param inflater  Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Instancia guardada
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {

            //Enlaza al XML del formulario y lo infla
            detailEmployeeBinding = DetailEmployeeBinding.inflate(inflater, container, false)

            //Referencia a la base de datos de Firebase
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("employees")

        } catch (exception: Exception) {
            Log.w(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
        return getDetailEmployeeBinding.root
    }

    /**
     * Hace editable el formulario
     */
    override fun makeFormEditable() {
        getDetailEmployeeBinding.apply {
            //Habilita los campos del formulario y los pinta de rojo
            arrayOf(
                name,
                surname,
                employeeDni,
                phone,
                address,
                birthdate,
                urlphoto,
                email,
                checkadmin
            ).forEach { view ->
                view.isEnabled = true
                view.setTextColor(editableEditTextColor)
            }

            birthdate.setOnClickListener {
                DatePickerFragment { day, month, year ->
                    birthdate.setText(String.format("$day/$month/$year"))
                }.show(parentFragmentManager, "datePicker")
            }
        }
    }

    /**
     * Rellena los datos del formulario con los datos
     */
    //Para que no de error al hacer el cast de la fecha
    override fun bindDataToForm() {
        CoroutineScope(Dispatchers.Main).launch {
            //Obtiene el empleado de la base de datos
            val employee: Employee? = documentSnapshot?.toObject(Employee::class.java)
            //Rellena los campos del formulario con los datos del empleado
            with(getDetailEmployeeBinding) {
                //Rellena los campos del formulario con los datos del empleado
                arrayOf(
                    Pair(employeeDni, employee?.dni),
                    Pair(name, employee?.name),
                    Pair(surname, employee?.surname),
                    Pair(employeeDni, employee?.dni),
                    Pair(phone, employee?.phone),
                    Pair(address, employee?.address),
                    Pair(urlphoto, employee?.photoURL),
                    Pair(email, employee?.email),
                    Pair(checkadmin, employee?.admin),
                    Pair(birthdate, employee?.birthdate)
                ).forEach { (field, valueToFill) ->
                    when (field) {
                        birthdate -> field.text = dateToStringFormat(valueToFill as Date)
                        checkadmin -> (field as CheckBox).isChecked = valueToFill as Boolean
                        else -> field.text = valueToFill.toString()
                    }
                }

                //Carga la foto en el formulario a partir de la URL almacenada
                //Si no hay foto, muestra una imagen por defecto. Usamos post para que se ejecute después
                //de que se haya cargado el formulario

                if (employee?.photoURL.toString().isEmpty()) {
                    employeeImage.post {
                        employeeImage.setImageResource(R.drawable.no_image_available)
                    }
                } else {
                    val bitmapFromUrl = Controller().getBitmapFromUrlAsync(
                        employee?.photoURL.toString()
                    ).await()
                    employeeImage.post {
                        employeeImage.setImageBitmap(bitmapFromUrl)
                    }
                }
            }
        }
    }


    /**
     * Devuelve un objeto de tipo empleado con los datos del formulario
     * @return Objeto de tipo empleado
     */
    override fun fillDataFromForm(): Employee {
        getDetailEmployeeBinding.apply {
            return Employee(
                employeeDni.text.toString(),
                name.text.toString(),
                surname.text.toString(),
                address.text.toString(),
                email.text.toString(),
                phone.text.toString(),
                Controller.stringToDateFormat(birthdate.text.toString()),
                urlphoto.text.toString(),
                checkadmin.isChecked
            )
        }
    }

    /**
     *  Al destruir la vista, elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        detailEmployeeBinding = null
    }
}
