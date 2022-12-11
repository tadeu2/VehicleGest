package es.ilerna.proyectodam.vehiclegest.ui.inspections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.models.ITV
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItvBinding

/**
 * Abre una ventana diálogo con los detalles
 */
class ItvDetail(s: DocumentSnapshot) : DetailFragment(s) {

    private var _binding: DetailItvBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Enlaza al XML del formulario y lo infla
        _binding = DetailItvBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance().collection("ITV")
        val root: View = binding.root

        //Escuchador del boton cerrar
        binding.bar.btclose.setOnClickListener {
            fragmentReplacer(ItvFragment())
        }

        //Escuchador del boton borrar
        binding.bar.btdelete.setOnClickListener {
            delDocument(s)
            fragmentReplacer(ItvFragment())
        }

        //Llama a la función que rellena los datos en el formulario
        bindData()

        return root
    }

    /**
     * Rellena los datos del formulario a partir de la ficha que hemos seleccionado
     */
    override fun bindData() {
        try {
            //Crea una instancia del objeto pasandole los datos de la instantanea de firestore
            val itv: ITV? = s.toObject(ITV::class.java)
            //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
            //El formato se puede modificar en strings.xml
            binding.date.setText(itv?.date?.let { Vehiclegest.customDateFormat(it) })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun editDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        _binding = null
    }
}