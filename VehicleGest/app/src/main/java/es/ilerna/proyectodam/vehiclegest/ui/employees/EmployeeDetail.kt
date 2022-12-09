package es.ilerna.proyectodam.vehiclegest.ui.employees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.backend.DetailFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Employee
import es.ilerna.proyectodam.vehiclegest.databinding.DetailEmployeeBinding

/**
 * Abre una ventana diálogo con los detalles
 */
class EmployeeDetail(s: DocumentSnapshot) : DetailFragment(s) {

    private var _binding: DetailEmployeeBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Enlaza al XML del formulario y lo infla
        _binding = DetailEmployeeBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance().collection("employees");
        val root: View = binding.root

        //Escuchador del boton cerrar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(EmployeeFragment())
        }

        //Escuchador del boton borrar
        binding.bar.btdelete.setOnClickListener {
            delDocument(s)
            fragmentReplacer(EmployeeFragment())
        }

        //Llama a la función que rellena los datos en el formulario
        bindData()
        return root
    }

    override fun bindData() {
        try {
            val employee: Employee? = s.toObject(Employee::class.java)
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
            binding.birthdate.setText(employee?.birthdate?.let { Vehiclegest.customDateFormat(it) })

            //Carga la foto en el formulario a partir de la URL almacenada
            //Vehiclegest.displayImgURL(employee?.photoURL.toString(), binding.employeeImage)
            // Mostrar la barra de carga
            progressBar = ProgressBar(context)
            //Carga la foto en el formulario a partir de la URL almacenada
            Controller().showImageFromUrl(
                binding.employeeImage,
                employee?.photoURL.toString(),
                progressBar
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun editDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}
