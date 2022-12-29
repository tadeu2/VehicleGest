package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.databinding.AddAlertBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.customReverseDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.AddFragment
import es.ilerna.proyectodam.vehiclegest.models.Alert
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class AddAlert : AddFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var _binding: AddAlertBinding? = null

    //Getter para la variable de enlace
    val binding get() = _binding!!

    /**
     * Fase de creación del fragmento
      */
    override fun onCreateView(
        //Variable de instancia de XML de vistas
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Referencia a la base de datos
        dbFirestoreReference = FirebaseFirestore.getInstance().collection("alert")

        //Enlaza al XML del formulario y lo infla
        _binding = AddAlertBinding.inflate(inflater, container, false)

        //Escuchador del botón de añadir
        binding.bar.btsave.setOnClickListener {
            addDocumentToDatabase()
            fragmentReplacer(AlertsFragment(), parentFragmentManager)
        }

        //Escuchador del botón de cancelar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(AlertsFragment(), parentFragmentManager)
        }

        binding.date.setOnClickListener {
            val newFragment =
                DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
            newFragment.show(parentFragmentManager, "datePicker")
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
                val date = customReverseDateFormat(binding.date.text.toString())
                val description = binding.alertDescription.text.toString()
                val solved = binding.checksolved.isChecked
                val alert = Alert(
                    plateNumber, date, description, solved
                )
                dbFirestoreReference.add(alert)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot escrito con ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error añadiendo documento", e)
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Fase de destrucción del fragmento
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }

}