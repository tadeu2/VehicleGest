package es.ilerna.proyectodam.vehiclegest.adapters

import android.util.Log
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

                    binding.employeeCard.setOnClickListener {
                        listener.onEmployeeSelected(snapshot)
                    }
                    Controller().showImageFromUrl(
                        binding.employeeImage,
                        employee?.photoURL.toString(),
                    )
                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString(), e)
                e.printStackTrace()
            } catch (e2: NullPointerException) {
                Log.e("Error", "Referencia nula", e2)
                e2.printStackTrace()
            }
        }
    }

    /**
     * Interfaz para implementar como se comportará al hacer click a una ficha
     */
    interface EmployeeAdapterListener {
        //Función que se ejecutará al hacer click en una ficha
        fun onEmployeeSelected(snapshot: DocumentSnapshot?)
        //Función que se encarga de añadir un registro
        fun onAddButtonClick()
    }

    /**
     * Llamada para devolver el item al viewholder por cada objeto de la lista s
     * @param parent Parámetro que contiene el padre
     * @param viewType Parámetro que contiene el tipo de vista
     * @return Devuelve el viewholder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder(
            //Inflamos la vista de la tarjeta
            EmployeeCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Función que se encarga de enlazar los datos con el holder
     * @param holder Parámetro que contiene el holder
     * @param position Parámetro que contiene la posición
     */
    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        //Obtenemos el empleado de la posición
        getSnapshot(position)?.let { snapshot ->
            //Llamamos a la función que pinta los datos en la tarjeta
            holder.bindDataCard(snapshot, listener)
        }

    }

}