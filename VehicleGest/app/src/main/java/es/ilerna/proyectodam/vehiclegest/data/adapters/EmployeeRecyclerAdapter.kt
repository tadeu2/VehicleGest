package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.data.entities.Employee
import es.ilerna.proyectodam.vehiclegest.databinding.EmployeeCardBinding
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentEmployeesBinding
import es.ilerna.proyectodam.vehiclegest.databinding.VehicleCardBinding
import java.util.*


/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class EmployeeRecyclerAdapter(
    query: Query,
    private val listener: EmployeeAdapterListener
) : FirestoreAdapter<EmployeeRecyclerAdapter.EmployeeViewHolder>(query) {

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class EmployeeViewHolder(
        private val binding: EmployeeCardBinding,

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
            binding.dni.text= employee?.dni.toString()
            binding.name.text = employee?.name.toString()
            binding.surname.text = employee?.surname.toString()
            //Foto del vehículo
            Glide.with(binding.root).load(employee?.photoURL).into(binding.employeeImage);

            binding.employeeCard.setOnClickListener {
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
        val binding = EmployeeCardBinding.inflate(layoutInflater, parent, false)
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