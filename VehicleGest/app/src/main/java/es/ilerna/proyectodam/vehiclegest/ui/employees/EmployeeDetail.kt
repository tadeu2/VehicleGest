package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.DetailEmployeeBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.customDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Employee
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del empleado
 * @param documentSnapshot Instantanea de firestore del empleado
 */
class EmployeeDetail(
    private val documentSnapshot: DocumentSnapshot
) : DetailFragment() {

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

            //Escuchador del boton cerrar
            getDetailEmployeeBinding.bar.btclose.setOnClickListener {
                fragmentReplacer(EmployeeFragment(), parentFragmentManager)
            }

            //Escuchador del boton borrar
            getDetailEmployeeBinding.bar.btdelete.setOnClickListener {
                delDocumentSnapshot(documentSnapshot)
                fragmentReplacer(EmployeeFragment(), parentFragmentManager)
            }

            //Escuchador del boton editar
            getDetailEmployeeBinding.bar.btsave.setOnClickListener {
                makeFormEditable() //Habilita el formulario para su edición
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
    private fun makeFormEditable() {
        getDetailEmployeeBinding.apply {
            name.isFocusableInTouchMode = true
            name.isCursorVisible = true
            surname.isFocusableInTouchMode = true
            surname.isCursorVisible = true
            employeeDni.isFocusableInTouchMode = true
            employeeDni.isCursorVisible = true
            phone.isFocusableInTouchMode = true
            phone.isCursorVisible = true
            address.isFocusableInTouchMode = true
            address.isCursorVisible = true
            birthdate.isFocusableInTouchMode = true
            birthdate.isCursorVisible = true
            url.isFocusableInTouchMode = true
            url.isCursorVisible = true
            email.isFocusableInTouchMode = true
            email.isCursorVisible = true
            checkadmin.isFocusableInTouchMode = true
            //Cambia el icono del boton
            val drawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_outline_save_24)
            bar.btsave.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            bar.btsave.setOnClickListener {
                updateDocumentToDatabase(documentSnapshot, fillDataFromForm())
                fragmentReplacer(EmployeeFragment(), parentFragmentManager)
            }
        }
    }

    /**
     * Rellena los datos del formulario con los datos
     */
    override fun bindDataToForm() {

        val employee: Employee? =
            documentSnapshot.toObject(Employee::class.java) //Obtiene el empleado de la base de datos
        with(
            getDetailEmployeeBinding
        ) { //Rellena los campos del formulario con los datos del empleado
            name.setText(employee?.name)
            surname.setText(employee?.surname)
            employeeDni.setText(employee?.dni)
            phone.setText(employee?.phone)
            birthdate.setText(employee?.birthdate?.let { customDateFormat(it) })
            url.setText(employee?.photoURL)
            checkadmin.isChecked = employee?.admin == true
            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            birthdate.setText(employee?.birthdate?.let { customDateFormat(it) })
        }

        //Carga la foto en el formulario a partir de la URL almacenada
        Controller().showImageFromUrl(
            getDetailEmployeeBinding.employeeImage,
            employee?.photoURL.toString(),
        )
    }

    /**
     * Devuelve un objeto de tipo empleado con los datos del formulario
     * @return Objeto de tipo empleado
     */
    override fun fillDataFromForm(): Employee {

        val birthdate = SimpleDateFormat(
            Vehiclegest.instance.getString(R.string.dateFormat),
            Locale.getDefault()
        ).parse(getDetailEmployeeBinding.birthdate.text.toString())

        getDetailEmployeeBinding.apply {
            return Employee(
                name.text.toString(),
                surname.text.toString(),
                employeeDni.text.toString(),
                phone.text.toString(),
                address.text.toString(),
                email.text.toString(),
                birthdate,
                url.text.toString(),
            )
        }
    }

    /**
     * Edita el documento de la base de datos
     * @param documentSnapshot Instantanea de firestore del empleado
     * @param any Objeto empleado con los datos del formulario
     */
    override fun updateDocumentToDatabase(documentSnapshot: DocumentSnapshot, any: Any) {
        Executors.newSingleThreadExecutor().execute {
            val documentReference = dbFirestoreReference.document(documentSnapshot.id)
            documentReference.set(any as Employee) //Actualiza el documento con los datos del formulario
                .addOnSuccessListener { Log.d("Vehiclegest", "Document successfully updated!") }
                .addOnFailureListener { e -> Log.w("vehiclegest", "Error updating document", e) }
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

    override fun addDocumentToDataBase() {
        //No implementable en este fragmento
    }

}
