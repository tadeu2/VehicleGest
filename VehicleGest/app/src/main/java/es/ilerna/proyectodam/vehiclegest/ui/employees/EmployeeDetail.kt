package es.ilerna.proyectodam.vehiclegest.ui.employees


import EmployeeFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.backend.DetailFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Employee
import es.ilerna.proyectodam.vehiclegest.databinding.DetailEmployeeBinding


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class EmployeeDetail(val data: Employee) : DetailFragment() {

    private var _binding: DetailEmployeeBinding? = null
    private val binding get() = _binding!!

    override fun bindData() {
        binding.employeeDni.text = data.dni
        binding.name.text = data.name
        binding.surname.text = data.surname
        binding.phone.text = data.phone
        binding.address.text = data.address
        binding.email.text = data.email
        binding.checkadmin.isChecked = data.admin!!

        //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
        //El formato se puede modificar en strings.xml
        binding.birthdate.text = data.birthdate?.let { Vehiclegest.customDateFormat(it) }

        //Carga la foto en el formulario a partir de la URL almacenada
        displayImgURL(data.photoURL, binding.employeeImage)

        binding.bar.btclose.setOnClickListener {
            onBtClose(EmployeeFragment())
        }
    }

    override fun editDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun delDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = DetailEmployeeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        bindData()
        return root
    }
}
