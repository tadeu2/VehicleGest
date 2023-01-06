package es.ilerna.proyectodam.vehiclegest.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.databinding.EmployeeCardBinding
import es.ilerna.proyectodam.vehiclegest.models.Employee
import java.security.AccessController.getContext
import java.util.concurrent.Executors

/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param queryFireStoreDatabase Parámetro que contiene la consulta a la base de datos
 * @param adapterListener Parámetro que contiene el listener del adapter
 */
class EmployeeRecyclerAdapter(
    queryFireStoreDatabase: Query,
    private val adapterListener: Controller.AdapterListener
) : FirestoreAdapter<EmployeeRecyclerAdapter.EmployeeViewHolder>(queryFireStoreDatabase) {

    /**
     * Clase interna
     * El holder se encarga de pintar las tarjetas de empleado
     * Implementa a RecyclerView.ViewHolder
     * @param employeeCardBinding Parámetro que contiene la vista de la tarjeta
     */
    class EmployeeViewHolder(
        private val employeeCardBinding: EmployeeCardBinding,
    ) : RecyclerView.ViewHolder(employeeCardBinding.root) {
        /**
         * Función que se encarga de pintar los datos en la tarjeta
         * @param documentSnapshot Parámetro que contiene la instancia del empleado
         * @param adapterListener Parámetro que contiene el listener de la tarjeta
         */
        fun bindDataToCardView(
            documentSnapshot: DocumentSnapshot,
            adapterListener: Controller.AdapterListener
        ) {
            try {
                //Crea un hilo paralelo para descargar las imagenes de una URL
                val executorService = Executors.newSingleThreadExecutor()
                executorService.execute {
                    //Inicializamos un objeto a partir de una instántanea
                    val employee: Employee? = documentSnapshot.toObject(Employee::class.java)

                    //La asignamos a los datos del formulario
                    employeeCardBinding.dni.text = employee?.dni.toString()
                    employeeCardBinding.name.text = employee?.name.toString()
                    employeeCardBinding.surname.text = employee?.surname.toString()

                    employeeCardBinding.employeeCard.setOnClickListener {
                        adapterListener.onItemSelected(documentSnapshot)
                    }
                    Controller().showImageFromUrl(
                        employeeCardBinding.employeeImage,
                        employee?.photoURL.toString(),
                    )
                    //Iniciamos el escuchador que accionamos al pulsar una ficha
                }
            } catch (exception: Exception) {
                Controller.mostrarToast(Vehiclegest.instance.applicationContext)
                Log.e("Error", exception.message.toString(), exception)
            }
        }
    }

    /**
     * Llamada para devolver el item al viewholder por cada objeto de la lista de datos
     * @param viewGroup Parámetro que contiene el padre de la vista de la tarjeta
     * @param viewType Parámetro que contiene el tipo de vista
     * @return Devuelve el viewholder con los datos del empleado en la tarjeta
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder(
            //Inflamos la vista de la tarjeta
            EmployeeCardBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    /**
     * Función que se encarga de enlazar los datos con el holder de la tarjeta de empleado
     *  y de llamar a la función que pinta los datos en la tarjeta
     * @param employeeViewHolder Parámetro que contiene el holder
     * @param position Parámetro que contiene la posición
     */
    override fun onBindViewHolder(employeeViewHolder: EmployeeViewHolder, position: Int) {
        //Obtenemos el empleado de la posición
        getSnapshot(position)?.let { documentSnapshot ->
            //Llamamos a la función que pinta los datos en la tarjeta
            employeeViewHolder.bindDataToCardView(documentSnapshot, adapterListener)
        }

    }

}