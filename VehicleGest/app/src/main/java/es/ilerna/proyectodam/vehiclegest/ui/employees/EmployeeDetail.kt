package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailEmployeeBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.dateToStringFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Employee

/**
 * Abre una ventana diálogo con los detalles del empleado
 * @param documentSnapshot Instantanea de firestore del empleado
 */
class EmployeeDetail(
    private val documentSnapshot: DocumentSnapshot
) : DetailModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailEmployeeBinding: DetailEmployeeBinding? = null
    private val getDetailEmployeeBinding
        get() = detailEmployeeBinding ?: throw IllegalStateException("Binding error")

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


            //Inicializa los escuchadores de los botones
            with(getDetailEmployeeBinding.bar) {
                btsave.visibility = View.GONE
                btedit.visibility = View.VISIBLE
                setListeners(
                    documentSnapshot,
                    parentFragmentManager,
                    EmployeeFragment(),
                    btclose,
                    btdelete,
                    btsave,
                    btedit
                )

            }
            //Llama a la función que rellena los datos en el formulario
            bindDataToForm()

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
            name.isEnabled = true
            name.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            surname.isEnabled = true
            surname.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            employeeDni.isEnabled = true
            employeeDni.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            phone.isEnabled = true
            phone.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            address.isEnabled = true
            address.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            birthdate.isEnabled = true
            birthdate.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            urlphoto.isEnabled = true
            urlphoto.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            email.isEnabled = true
            email.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
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

    /**
     * Rellena los datos del formulario con los datos
     */
    override fun bindDataToForm() {
        //Obtiene el empleado de la base de datos
        val employee: Employee? = documentSnapshot.toObject(Employee::class.java)
        //Rellena los campos del formulario con los datos del empleado
        with(getDetailEmployeeBinding) {

            name.setText(employee?.name)
            surname.setText(employee?.surname)
            employeeDni.setText(employee?.dni)
            phone.setText(employee?.phone)
            address.setText(employee?.address)
            urlphoto.setText(employee?.photoURL)
            email.setText(employee?.email)
            checkadmin.isChecked = employee?.admin == true
            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            birthdate.setText(dateToStringFormat(employee?.birthdate))

            //Carga la foto en el formulario a partir de la URL almacenada
            //Si no hay foto, muestra una imagen por defecto. Usamos post para que se ejecute después
            //de que se haya cargado el formulario
            employeeImage.post {
                if (employee?.photoURL.toString().isNullOrEmpty()) {
                    employeeImage.setImageResource(R.drawable.no_image_available)
                } else {
                    Controller().showImageFromUrl(
                        employeeImage,
                        employee?.photoURL.toString()
                    )
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
