package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.data.entities.Employee
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentEmployeesBinding
import java.util.*


/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class EmployeeListAdapter(
    query: Query,
    private val listener: EmployeeAdapterListener
) : FirestoreAdapter<EmployeeListAdapter.EmployeeViewHolder>(query) {

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class EmployeeViewHolder(
        private val binding: FragmentEmployeesBinding,

        ) : ViewHolder(binding.root) {

        fun bind(snapshot: DocumentSnapshot, listener: EmployeeAdapterListener) {
            val employee: Employee? = snapshot.toObject(Employee::class.java)
            assignData(employee, listener)
        }

        /**
         * Rellena cada item de la tarjeta con los datos del objeto vehiculo
         * @param employee Ficha de cada vehículo
         */
        private fun assignData(employee: Employee?, listener: EmployeeAdapterListener) {
            binding.employeeDni.text = employee?.dni.toString()
            binding.employeeName.text = employee?.name.toString()
            binding.employeeSurname.text = employee?.surname.toString()

          /*  //Formatea los timestamp según el string de recursos.
            val simpleDateFormat = SimpleDateFormat(
                Vehiclegest.appContext().resources
                    .getString(R.string.dateFormat), Locale.getDefault()
            )
            val stamp = employee?.expiryDateITV?.time
            val date = simpleDateFormat.format(Date(stamp!!))

            binding.expirydateitv.text = date*/

            /*binding.totaldistance.text = buildString {
                append(vehicle.totalDistance.toString())
                append(" KM")
            }*/
            binding.employeeItemList.setOnClickListener {
                listener.onEmployeeSelected(employee)
            }
        }
    }


    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface EmployeeAdapterListener {
        fun onEmployeeSelected(employee: Employee??)
    }


    /**
     * Llamada para devolver el item(VehicleCard) al viewholder por cada objeto de la lista vehiculos
     *
     */
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentEmployeesBinding.inflate(layoutInflater, parent, false)
        return EmployeeViewHolder(binding)
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     */
    @Override
    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }

}