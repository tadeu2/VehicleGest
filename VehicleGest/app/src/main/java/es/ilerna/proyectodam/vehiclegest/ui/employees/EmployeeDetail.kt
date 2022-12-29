package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.databinding.DetailEmployeeBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.customDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Employee

/**
 * Abre una ventana diálogo con los detalles
 */
class EmployeeDetail(
    private val snapshot: DocumentSnapshot
) : DetailFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var _binding: DetailEmployeeBinding? = null
    private val binding get() = _binding!!

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

        //Enlaza al XML del formulario y lo infla
        _binding = DetailEmployeeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Escuchador del boton cerrar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(EmployeeFragment(), parentFragmentManager)
        }

        //Escuchador del boton borrar
        binding.bar.btdelete.setOnClickListener {
            delDocument(snapshot)
            fragmentReplacer(EmployeeFragment(), parentFragmentManager)
        }

        //Llama a la función que rellena los datos en el formulario
        bindDataToForm()
        return root
    }

    /**
     * Rellena los datos del formulario con los datos
     */
    override fun bindDataToForm() {
        try {
            val employee: Employee? = snapshot.toObject(Employee::class.java)
            binding.url.setText(employee?.photoURL)
            binding.employeeDni.setText(employee?.dni)
            binding.name.setText(employee?.name)
            binding.surname.setText(employee?.surname)
            binding.phone.setText(employee?.phone)
            binding.address.setText(employee?.address)
            binding.email.setText(employee?.email)
            binding.checkadmin.isChecked = employee?.admin == false

            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            binding.birthdate.setText(employee?.birthdate?.let { customDateFormat(it) })
            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(
                binding.employeeImage,
                employee?.photoURL.toString(),
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Edita el documento de la base de datos
     */
    override fun editDocument(snapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    /**
     *  Al destruir la vista, elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}
