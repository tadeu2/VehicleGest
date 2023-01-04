package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.databinding.DetailEmployeeBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.customDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Employee

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

            //Escuchador del boton cerrar
            getDetailEmployeeBinding.bar.btclose.setOnClickListener {
                fragmentReplacer(EmployeeFragment(), parentFragmentManager)
            }

            //Escuchador del boton borrar
            getDetailEmployeeBinding.bar.btdelete.setOnClickListener {
                delDocumentSnapshot(documentSnapshot)
                fragmentReplacer(EmployeeFragment(), parentFragmentManager)
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
     * Rellena los datos del formulario con los datos
     */
    override fun bindDataToForm() {
        val employee: Employee? = documentSnapshot.toObject(Employee::class.java)
        getDetailEmployeeBinding.url.setText(employee?.photoURL)
        getDetailEmployeeBinding.employeeDni.setText(employee?.dni)
        getDetailEmployeeBinding.name.setText(employee?.name)
        getDetailEmployeeBinding.surname.setText(employee?.surname)
        getDetailEmployeeBinding.phone.setText(employee?.phone)
        getDetailEmployeeBinding.address.setText(employee?.address)
        getDetailEmployeeBinding.email.setText(employee?.email)
        getDetailEmployeeBinding.checkadmin.isChecked = employee?.admin == false

        //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
        //El formato se puede modificar en strings.xml
        getDetailEmployeeBinding.birthdate.setText(employee?.birthdate?.let { customDateFormat(it) })
        //Carga la foto en el formulario a partir de la URL almacenada
        Controller().showImageFromUrl(
            getDetailEmployeeBinding.employeeImage,
            employee?.photoURL.toString(),
        )

    }

    /**
     * Edita el documento de la base de datos
     * @param documentSnapshot Instantanea de firestore del empleado
     */
    override fun editDocumentSnapshot(documentSnapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
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
