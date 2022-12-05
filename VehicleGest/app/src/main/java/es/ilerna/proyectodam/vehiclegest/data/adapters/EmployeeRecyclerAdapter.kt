package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Employee
import es.ilerna.proyectodam.vehiclegest.databinding.EmployeeCardBinding
import java.util.concurrent.Executors


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

        /**
         * Rellena cada item de la tarjeta con los datos del objeto empleado
         */
        fun bind(
            snapshot: DocumentSnapshot,
            listener: EmployeeAdapterListener
        ) {
            try {

                //Crea un hilo paralelo para descargar las imagenes de una URL
                val executor = Executors.newSingleThreadExecutor()
                executor.execute {

                    //Inicializamos un objeto a partir de una instántanea
                    val employee: Employee? = snapshot.toObject(Employee::class.java)
                    //La asignamos a los datos del formulario
                    binding.dni.text = employee?.dni.toString()
                    binding.name.text = employee?.name.toString()
                    binding.surname.text = employee?.surname.toString()

                    //Carga la foto en el formulario a partir de la URL almacenada
                    Vehiclegest.displayImgURL(employee?.photoURL.toString(), binding.employeeImage)

                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                    binding.employeeCard.setOnClickListener {
                        listener.onEmployeeSelected(snapshot)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface EmployeeAdapterListener {
        fun onEmployeeSelected(snapshot: DocumentSnapshot?)
    }

    /**
     * Llamada para devolver el item al viewholder por cada objeto de la lista s
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