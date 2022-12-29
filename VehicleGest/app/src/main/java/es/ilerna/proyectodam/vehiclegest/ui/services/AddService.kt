package es.ilerna.proyectodam.vehiclegest.ui.services

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.databinding.AddServiceBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
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
    private var _binding: AddServiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbService: CollectionReference

    /**
     * Fase de creación de la vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        //Inicializa la base de datos
        dbService = FirebaseFirestore.getInstance().collection("service");

        //Enlaza al XML del formulario y lo infla
        _binding = AddServiceBinding.inflate(inflater, container, false)

        //Escucha el botón de añadir
        binding.bar.btsave.setOnClickListener() {
            addDocumentToDatabase()
            fragmentReplacer(ServiceFragment(), parentFragmentManager)
        }
        //Escuchador del botón de cancelar
        binding.bar.btclose.setOnClickListener() {
            fragmentReplacer(ServiceFragment(), parentFragmentManager)
        }

        //Escucha el botón de fecha
        binding.date.setOnClickListener() {
            //Crea un nuevo fragmento de diálogo
            DatePickerFragment { day, month, year ->
                // Actualiza el campo de fecha
                binding.date.setText(String.format("$day/$month/$year"))
            }
                // Muestra el diálogo
                .show(parentFragmentManager, "datePicker")
        }

        //Llama a la función que rellena los datos en el formulario
        return binding.root
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        binding.date.setText(String.format("$day/$month/$year"))
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addDocumentToDatabase() {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val plateNumber = binding.plateNumber.text.toString()
                // Devuelve la fecha en formato dd/mm/yyyy
                val date = SimpleDateFormat(
                    Vehiclegest.instance.resources.getString(R.string.dateFormat),
                    Locale.getDefault()
                ).parse(binding.date.text.toString()) as Date
                val remarks = binding.remarks.text.toString()
                val costumer = binding.costumer.text.toString()

                val service = Service(
                    plateNumber, date, remarks, costumer
                )
                dbService.add(service).addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot escrito con ID: ${documentReference.id}")
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error añadiendo documento", e)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } catch (e2: NullPointerException) {
                e2.printStackTrace()
            }
        }
    }

    /**
     * Elimina la vista del formulario
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }

}
