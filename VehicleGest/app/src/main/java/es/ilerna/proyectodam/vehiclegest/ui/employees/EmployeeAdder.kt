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
import es.ilerna.proyectodam.vehiclegest.databinding.DetailEmployeeBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Employee

/**
 * Abre una ventana para añadir un empleado
 */
class EmployeeAdder : DetailModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var addEmployeeBinding: DetailEmployeeBinding? = null

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
            addEmployeeBinding = DetailEmployeeBinding.inflate(inflater, container, false)
            //Referencia a la base de datos de Firebase
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("employees")

            //Carga la foto en el formulario a partir de la URL almacenada
            getAddEmployeeBinding.urlphoto.doAfterTextChanged {
                //Carga la foto en el formulario a partir de la URL almacenada
                Controller().showImageFromUrl(
                    getAddEmployeeBinding.employeeImage,
                    getAddEmployeeBinding.urlphoto.text.toString()
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
     * Rellena los datos del formulario a partir del formulario
     * @return Devuelve un objeto de tipo empleado
     */
    override fun fillDataFromForm(): Any {
        getAddEmployeeBinding.apply {
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
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        //No se implementa en este fragmento
    }

    override fun updateDocumentToDatabase(documentSnapshot: DocumentSnapshot, any: Any) {
        //No se implementa en este fragmento
    }

    override fun bindDataToForm() {
        //No se implementa en este fragmento
    }


}