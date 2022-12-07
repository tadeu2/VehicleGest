package es.ilerna.proyectodam.vehiclegest.ui.inspections

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.backend.AddFragment
import es.ilerna.proyectodam.vehiclegest.backend.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.data.entities.ITV
import es.ilerna.proyectodam.vehiclegest.databinding.AddItvBinding
import java.util.concurrent.Executors

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class AddItv : AddFragment() {

    private var _binding: AddItvBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbItv: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dbItv = FirebaseFirestore.getInstance().collection("ITV");

        //Enlaza al XML del formulario y lo infla
        _binding = AddItvBinding.inflate(inflater, container, false)

        binding.bar.btsave.setOnClickListener() {
            addData()
            fragmentReplacer(ItvFragment(), parentFragmentManager)
        }

        binding.bar.btclose.setOnClickListener() {
            fragmentReplacer(ItvFragment(), parentFragmentManager)
        }

        binding.date.setOnClickListener() {
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
    override fun addData() {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {

                val date =
                    Vehiclegest.customReverseDateFormat(binding.date.text.toString())

                val itv = ITV(
                    date
                )
                dbItv.add(itv)
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

    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }

}