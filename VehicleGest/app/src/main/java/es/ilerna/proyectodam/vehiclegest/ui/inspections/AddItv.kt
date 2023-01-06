package es.ilerna.proyectodam.vehiclegest.ui.inspections

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.AddItvBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.customReverseDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.ITV
import java.util.concurrent.Executors

/**
 * Clase que representa el fragmento de añadir ITV
 */
class AddItv : DetailFragment() {

    //Enlace al xml de la interfaz
    private var addItvBinding: AddItvBinding? = null

    //Getter para el binding
    private val getAddItvBinding
        get() = addItvBinding ?: throw IllegalStateException("Binding error")

    /**
     * Fase de creación del fragmento
     * @param inflater Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Estado de la instancia
     * @return Vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        try {

            //Referencia a la base de datos de Firestore
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("ITV")

            //Enlaza al XML del formulario y lo infla
            addItvBinding = AddItvBinding.inflate(inflater, container, false)

            //Escuchador del botón de añadir
            getAddItvBinding.bar.btsave.setOnClickListener{
                addDocumentToDataBase()
                fragmentReplacer(ItvFragment(), parentFragmentManager)
            }

            //Escuchador del botón de cancelar
            getAddItvBinding.bar.btclose.setOnClickListener {
                fragmentReplacer(ItvFragment(), parentFragmentManager)
            }

            //Escuchador del botón de fecha
            getAddItvBinding.date.setOnClickListener {
                DatePickerFragment { day, month, year -> String.format("$day/$month/$year") }
                    .show(parentFragmentManager, "datePicker")
            }
        } catch (exception: Exception) {
            Log.e(TAG, exception.message.toString(), exception)
        }
        //Llama a la función que rellena los datos en el formulario
        return getAddItvBinding.root
    }

    override fun bindDataToForm() {
        TODO("Not yet implemented")
    }

    override fun fillDataFromForm(): Any {
        TODO("Not yet implemented")
    }


    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun addDocumentToDataBase() {
        Executors.newSingleThreadExecutor().execute {
            val date = customReverseDateFormat(getAddItvBinding.date.text.toString())
            val itv = ITV(
                date
            )
            dbFirestoreReference.add(itv).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot escrito con ID: ${documentReference.id}")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error añadiendo documento", e)
            }
        }
    }

    override fun updateDocumentToDatabase(documentSnapshot: DocumentSnapshot, any: Any) {
        TODO("Not yet implemented")
    }

    /**
     * Fase de destrucción del fragmento y eliminación de la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        addItvBinding = null
    }

}