package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailEmployeeBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
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

            makeFormEditable() //Habilita los campos para su edición
            with(addEmployeeBinding!!.bar) {
                btdelete.visibility = View.GONE //Oculta el botón de eliminar
                btedit.visibility = View.GONE //Oculta el botón de editar
                btsave.visibility = View.VISIBLE //Muestra el botón de guardar
                setListeners(
                    null,
                    parentFragmentManager,
                    EmployeeFragment(),
                    btclose,
                    btdelete,
                    btsave,
                    btedit
                ) //Inicializa los escuchadores de los botones
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
        getAddEmployeeBinding.apply {
            employeeDni.isEnabled = true
            employeeDni.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            name.isEnabled = true
            name.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            surname.isEnabled = true
            surname.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            address.isEnabled = true
            address.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            email.isEnabled = true
            email.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            phone.isEnabled = true
            phone.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            birthdate.isEnabled = true
            birthdate.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            urlphoto.isEnabled = true
            urlphoto.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            checkadmin.isEnabled = true
            checkadmin.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))

            //Escuchador del botón de fecha
            birthdate.setOnClickListener {
                //Abre el selector de fecha
                DatePickerFragment { day, month, year ->
                    //Muestra la fecha en el campo de texto
                    birthdate.setText(String.format("$day/$month/$year"))
                }.show(parentFragmentManager, "datePicker")
            }
        }
    }

    override fun bindDataToForm() {
        //No se implementa en este fragmento
    }
}