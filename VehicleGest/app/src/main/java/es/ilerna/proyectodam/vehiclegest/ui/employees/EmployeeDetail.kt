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
class EmployeeDetail(s: DocumentSnapshot) : DetailFragment(s) {

    private var _binding: DetailEmployeeBinding? = null
    private val binding get() = _binding!!

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
            Vehiclegest.displayImgURL(employee?.photoURL.toString(), binding.employeeImage)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun editDocument(s: DocumentSnapshot) {
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
