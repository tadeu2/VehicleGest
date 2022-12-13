package es.ilerna.proyectodam.vehiclegest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.backend.Controller
import es.ilerna.proyectodam.vehiclegest.databinding.EmployeeCardBinding
import es.ilerna.proyectodam.vehiclegest.models.Employee
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param query Parámetro que contiene la consulta a la base de datos
 * @param listener Parámetro que contiene el listener del adapter
 */
class EmployeeRecyclerAdapter(
    query: Query,
    private val listener: EmployeeAdapterListener
) : FirestoreAdapter<EmployeeRecyclerAdapter.EmployeeViewHolder>(query) {

    /**
     * Clase interna
     * El holder se encarga de pintar las tarjetas de empleado
     * Implementa a RecyclerView.ViewHolder
     * @param binding Parámetro que contiene la vista de la tarjeta
     */
    class EmployeeViewHolder(
        private val binding: EmployeeCardBinding,
    ) : ViewHolder(binding.root) {

        private lateinit var progressBar: ProgressBar

        /**
         * Función que se encarga de pintar los datos en la tarjeta
         * @param snapshot Parámetro que contiene la instancia del empleado
         * @param listener Parámetro que contiene el listener de la tarjeta
         */
        fun bindDataCard(
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
                    //Vehiclegest.displayImgURL(employee?.photoURL.toString(), binding.employeeImage)
                    // Mostrar la barra de carga
                    progressBar = ProgressBar(binding.root.context)

                    binding.employeeCard.setOnClickListener {
                        listener.onEmployeeSelected(snapshot)
                    }
                    Controller().showImageFromUrl(
                        binding.employeeImage,
                        employee?.photoURL.toString(),
                        progressBar
                    )
                    //Iniciamos el escuchador que accionamos al pulsar una ficha
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
            holder.bindDataCard(snapshot, listener)
        }
    }

}