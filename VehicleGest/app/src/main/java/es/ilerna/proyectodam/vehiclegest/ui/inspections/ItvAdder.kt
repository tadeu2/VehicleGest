package es.ilerna.proyectodam.vehiclegest.ui.inspections

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItvBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.stringToDateFormat
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.ITV

/**
 * Clase que representa el fragmento de añadir ITV
 */
class ItvAdder : DetailModelFragment() {

    //Enlace al xml de la interfaz
    private var addItvBinding: DetailItvBinding? = null

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
            addItvBinding = DetailItvBinding.inflate(inflater, container, false)

            //Añade los listeners a los botones
            with(getAddItvBinding.bar) {
                btsave.visibility = View.VISIBLE
                btedit.visibility = View.GONE
            setListeners(
                null,
                parentFragmentManager,
                ItvFragment(),
                btclose,
                btdelete,
                btsave,
                btedit
            )}

        } catch (exception: Exception) {
            Log.e(TAG, exception.message.toString(), exception)
        }
        //Llama a la función que rellena los datos en el formulario
        return getAddItvBinding.root
    }

    /**
     * Función que pasa los datos al formulario
     */
    override fun bindDataToForm() {
        //No se implementa en este fragmento
    }

    /**
     * Funcion que rellema los datos desde el formulario
     */
    override fun fillDataFromForm(): Any {
        getAddItvBinding.apply {
            //Rellena los datos del formulario
            return ITV(
                stringToDateFormat(date.text.toString())
            )
        }
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        //No se implementa en este fragmento
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