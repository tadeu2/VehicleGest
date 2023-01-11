package es.ilerna.proyectodam.vehiclegest.ui.inspections

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItvBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.dateToStringFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.stringToDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.ITV

/**
 * Abre una ventana diálogo con los detalles de la ITV
 * @param documentSnapshot Instantanea de firestore de la ITV
 */
class ItvDetail(
    private val documentSnapshot: DocumentSnapshot
) : DetailModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailItvBinding: DetailItvBinding? = null
    private val getDetailItvBinding
        get() = detailItvBinding ?: throw IllegalStateException("Binding error")

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

            //Iniciliza los escuchadores de los botones
            with(getDetailItvBinding.bar) {
                btsave.visibility = View.VISIBLE
                btedit.visibility = View.GONE
                setListeners(
                    documentSnapshot,
                    parentFragmentManager,
                    ItvFragment(),
                    btclose,
                    btdelete,
                    btsave,
                    btedit,
                )
            }

            //Llama a la función que rellena los datos en el formulario
            bindDataToForm()
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
        }
        return getDetailItvBinding.root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun bindDataToForm() {
        //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
        val itv: ITV? = documentSnapshot.toObject(ITV::class.java)
        //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
        //El formato se puede modificar en strings.xml
        getDetailItvBinding.date.setText(dateToStringFormat(itv?.date))
    }

    override fun fillDataFromForm(): Any {
        getDetailItvBinding.apply {
            return ITV(
                stringToDateFormat(date.text.toString())
            )
        }
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        getDetailItvBinding.apply {
            date.isFocusableInTouchMode = true

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