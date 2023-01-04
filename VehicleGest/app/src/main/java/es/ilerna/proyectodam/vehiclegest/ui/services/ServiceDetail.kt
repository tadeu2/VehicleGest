package es.ilerna.proyectodam.vehiclegest.ui.services

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.databinding.DetailServiceBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.customDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Service

/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param documentSnapshot Instantanea de firestore del servicio
 */
class ServiceDetail(
    private val documentSnapshot: DocumentSnapshot
) : DetailFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var detailServiceBinding: DetailServiceBinding? = null
    private val getDetailServiceBinding
        get() = detailServiceBinding ?: throw IllegalStateException(
            "Binding error"
        )  // Si no se enlaza el XML con el código, lanza una excepción

    /**
     *  Fase de creación de la vista
     *  @param inflater Inflador de la vista
     *  @param container Contenedor de la vista
     *  @param savedInstanceState Instancia guardada
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza al XML del formulario y lo infla
            detailServiceBinding = DetailServiceBinding.inflate(inflater, container, false)

            //Escuchador del boton cerrar
            getDetailServiceBinding.bar.btclose.setOnClickListener {
                fragmentReplacer(ServiceFragment(), parentFragmentManager)
            }

            //Escuchador del boton borrar, borrará el servicio y volverá al fragmento de servicios
            getDetailServiceBinding.bar.btdelete.setOnClickListener {
                delDocumentSnapshot(documentSnapshot)
                fragmentReplacer(ServiceFragment(), parentFragmentManager)
            }

            //Llama a la función que rellena los datos en el formulario
            bindDataToForm()
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
        }
        return getDetailServiceBinding.root
    }

    /**
     * Rellena los datos del formulario con los datos del servicio
     */
    override fun bindDataToForm() {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val service: Service? = documentSnapshot.toObject(Service::class.java)
            getDetailServiceBinding.plateNumber.setText(service?.plateNumber.toString())
            getDetailServiceBinding.costumer.setText(service?.costumer.toString())
            getDetailServiceBinding.remarks.setText(service?.remarks.toString())

            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            getDetailServiceBinding.date.setText(service?.date?.let { customDateFormat(it) })

    }

    /**
     * Edita los datos del servicio
     * @param documentSnapshot Instantanea de firestore del servicio
     */
    override fun editDocumentSnapshot(documentSnapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    /**
     * Al destruir la vista, elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        detailServiceBinding = null
    }

}