package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.databinding.DetailAlertBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.customDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.Alert

/**
 * Abre una ventana diálogo con los detalles del vehículo
 * @param snapshot Instantanea de firestore de la alerta
 */
class AlertDetail(
    val snapshot: DocumentSnapshot,
) :
    DetailFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var _binding: DetailAlertBinding? = null
    private val binding get() = _binding!!

    //Crea la vista
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        //Enlaza al XML del formulario y lo infla
        _binding = DetailAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Escuchador del boton cerrar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(AlertsFragment(), parentFragmentManager)
        }

        //Escuchador del boton borrar
        binding.bar.btdelete.setOnClickListener {
            delDocument(snapshot)
            fragmentReplacer(AlertsFragment(), parentFragmentManager)
        }

        //Llama a la función que rellena los datos en el formulario
        bindDataToForm()
        return root
    }


    /**
     * Rellena los datos del formulario con los datos
     */
    override fun bindDataToForm() {

        try {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val alert: Alert? = snapshot.toObject(Alert::class.java)
            binding.plateNumber.setText(alert?.plateNumber)
            binding.alertDescription.setText(alert?.description)
            binding.checksolved.isChecked = alert?.solved == false

            //Formatea los timestamp a fecha normal dd/mm/aa
            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            binding.date.setText(alert?.date?.let { customDateFormat(it) })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Edita los datos del documento en firestore
     */
    override fun editDocument(snapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    /**
     *  Al destrozar la vista, elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}