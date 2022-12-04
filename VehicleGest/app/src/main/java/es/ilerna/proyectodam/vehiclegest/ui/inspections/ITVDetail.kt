package es.ilerna.proyectodam.vehiclegest.ui.inspections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.backend.DetailFragment
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.ITV
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItvBinding

/**
 * Abre una ventana diálogo con los detalles
 */
class ItvDetail(val data: ITV) : DetailFragment() {

    private var _binding: DetailItvBinding? = null
    private val binding get() = _binding!!

    override fun bindData() {
        //Usa la función creada en Vehiclegest para dar formato a las fechas dadas en timestamp
        //El formato se puede modificar en strings.xml
        binding.date.text = data.date?.let { Vehiclegest.customDateFormat(it) }

        binding.bar.btclose.setOnClickListener {
            this.onBtClose(ITVFragment())
        }
    }

    override fun editDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun delDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DetailItvBinding.inflate(inflater, container, false)
        val root: View = binding.root
        bindData()
        return root
    }
}