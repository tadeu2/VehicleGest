package es.ilerna.proyectodam.vehiclegest.ui.inspections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItvBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.customDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.models.ITV

/**
 * Abre una ventana diálogo con los detalles
 */
class ItvDetail(
    val snapshot: DocumentSnapshot
) : DetailFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var _binding: DetailItvBinding? = null
    private val binding get() = _binding!!

    //Fase de creación del fragmento
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Enlaza al XML del formulario y lo infla
        _binding = DetailItvBinding.inflate(inflater, container, false)
        //db = FirebaseFirestore.getInstance().collection("ITV")
        val root: View = binding.root

        //Escuchador del boton cerrar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(ItvFragment(), parentFragmentManager)
        }

        //Escuchador del boton borrar
        binding.bar.btdelete.setOnClickListener {
            delDocument(snapshot)
            fragmentReplacer(ItvFragment(), parentFragmentManager)
        }

        //Llama a la función que rellena los datos en el formulario
        bindDataToForm()

        return root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun bindDataToForm() {
        try {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val itv: ITV? = snapshot.toObject(ITV::class.java)
            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            binding.date.setText(itv?.date?.let { customDateFormat(it) })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *  Edita los datos de la ficha seleccionada
     */
    override fun editDocument(snapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    /**
     * Al destruir la vista, se elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}