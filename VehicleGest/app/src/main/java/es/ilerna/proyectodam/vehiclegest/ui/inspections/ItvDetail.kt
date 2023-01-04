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
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.customDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.ITV

/**
 * Abre una ventana diálogo con los detalles de la ITV
 * @param documentSnapshot Instantanea de firestore de la ITV
 */
class ItvDetail(
    private val documentSnapshot: DocumentSnapshot
) : DetailFragment() {

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

            //Escuchador del boton cerrar
            getDetailItvBinding.bar.btclose.setOnClickListener {
                fragmentReplacer(ItvFragment(), parentFragmentManager)
            }

            //Escuchador del boton borrar
            getDetailItvBinding.bar.btdelete.setOnClickListener {
                delDocumentSnapshot(documentSnapshot)
                fragmentReplacer(ItvFragment(), parentFragmentManager)
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
        getDetailItvBinding.date.setText(itv?.date?.let { customDateFormat(it) })
    }

    /**
     *  Edita los datos de la ficha seleccionada
     *  @param documentSnapshot Instantanea de firestore de la ITV
     */
    override fun editDocumentSnapshot(documentSnapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
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