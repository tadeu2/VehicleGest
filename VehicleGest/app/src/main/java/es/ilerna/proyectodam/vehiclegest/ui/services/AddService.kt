package es.ilerna.proyectodam.vehiclegest.ui.services

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.AddServiceBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.AddFragment
import es.ilerna.proyectodam.vehiclegest.models.Service
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del vehículo
 *
 */
class AddService : AddFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var addServiceBinding: AddServiceBinding? = null
    private val getAddServiceBinding
        get() = addServiceBinding ?: throw IllegalStateException("Binding error")

    /**
     * Fase de creación de la vista del fragmento
     * @param inflater LayoutInflater para inflar la vista
     * @param container ViewGroup para la vista
     * @param savedInstanceState Bundle de datos
     * @return Vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Inicializa la base de datos
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("service");

            //Enlaza al XML del formulario y lo infla
            addServiceBinding = AddServiceBinding.inflate(inflater, container, false)

            //Escucha el botón de añadir
            getAddServiceBinding.bar.btsave.setOnClickListener() {
                addDocumentToDatabase()
                fragmentReplacer(ServiceFragment(), parentFragmentManager)
            }
            //Escuchador del botón de cancelar
            getAddServiceBinding.bar.btclose.setOnClickListener() {
                fragmentReplacer(ServiceFragment(), parentFragmentManager)
            }

            //Escucha el botón de fecha
            getAddServiceBinding.date.setOnClickListener() {
                //Crea un nuevo fragmento de diálogo
                DatePickerFragment { day, month, year ->
                    // Actualiza el campo de fecha
                    getAddServiceBinding.date.setText(String.format("$day/$month/$year"))
                }
                    // Muestra el diálogo
                    .show(parentFragmentManager, "datePicker")
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e(TAG, "Error al crear la vista del fragmento", exception)
        }
        return getAddServiceBinding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addDocumentToDatabase() {
        Executors.newSingleThreadExecutor().execute {
            val plateNumber = getAddServiceBinding.plateNumber.text.toString()
            // Devuelve la fecha en formato dd/mm/yyyy
            val date = SimpleDateFormat(
                Vehiclegest.instance.resources.getString(R.string.dateFormat),
                Locale.getDefault()
            ).parse(getAddServiceBinding.date.text.toString()) as Date
            val remarks = getAddServiceBinding.remarks.text.toString()
            val costumer = getAddServiceBinding.costumer.text.toString()

            val service = Service(
                plateNumber, date, remarks, costumer
            )
            dbFirestoreReference.add(service).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot escrito con ID: ${documentReference.id}")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error añadiendo documento", e)
            }
        }
    }

    /**
     * Elimina la vista del formulario cuando se destruye el fragmento
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        addServiceBinding = null
    }

}
