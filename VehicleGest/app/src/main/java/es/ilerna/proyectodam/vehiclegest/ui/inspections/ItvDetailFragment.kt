package es.ilerna.proyectodam.vehiclegest.ui.inspections

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItvBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.dateToStringFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.stringToDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.models.ITV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Abre una ventana diálogo con los detalles de la ITV
 */
class ItvDetailFragment : DetailFormModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailItvBinding: DetailItvBinding? = null
    private val getDetailItvBinding
        get() = detailItvBinding ?: throw IllegalStateException("Binding error")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Crea una instancia del fragmento principal para poder volver a él
        mainFragment = ItvFragment()
    }

    /**
     * Fase de creación de la vista
     * @param inflater  Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Instancia guardada7
     * @return Vista de la interfaz
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        try {
            //Enlaza al XML del formulario y lo infla
            detailItvBinding = DetailItvBinding.inflate(inflater, container, false)
            //Referencia a la base de datos
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("ITV")

        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
        }
        return getDetailItvBinding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun bindDataToForm() {
        CoroutineScope(Dispatchers.Main).launch {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val itv: ITV? = documentSnapshot?.toObject(ITV::class.java)
            with(getDetailItvBinding) {
                //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
                //El formato se puede modificar en strings.xml
                arrayOf(
                    Pair(date, itv?.date),
                    Pair(remarks, itv?.remarks)
                ).forEach { (field, valueToFill) ->
                    if (field == date) {
                        field.setText(dateToStringFormat(valueToFill as Date?))
                    } else {
                        field.setText(valueToFill.toString())
                    }
                }
            }
        }
    }

    override fun fillDataFromForm(): Any {
        getDetailItvBinding.apply {
            return ITV(
                stringToDateFormat(date.text.toString()),
                remarks.text.toString()
            )
        }
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        getDetailItvBinding.apply {
            //Habilita los campos de texto y los pinta de rojo
           arrayOf(date, remarks).forEach { view ->
                view.isEnabled = true
                view.setTextColor(editableEditTextColor)
            }

            //Escuchador del botón de fecha
            date.setOnClickListener {
                //Abre el selector de fecha
                DatePickerFragment { day, month, year ->
                    //Muestra la fecha en el campo de texto
                    date.setText(String.format("$day/$month/$year"))
                }.show(parentFragmentManager, "datePicker")
            }
        }
    }

    /**
     * Al destruir la vista, se elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        detailItvBinding = null
    }
}