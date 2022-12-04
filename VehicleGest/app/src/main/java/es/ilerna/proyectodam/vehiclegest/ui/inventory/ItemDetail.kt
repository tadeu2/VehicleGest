package es.ilerna.proyectodam.vehiclegest.ui.inventory


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.DetailFragment
import es.ilerna.proyectodam.vehiclegest.data.entities.Item
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItemBinding
import java.net.URL
import java.util.concurrent.Executors


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class ItemDetail(val data: Item) : DetailFragment() {

    private var _binding: DetailItemBinding? = null
    private val binding get() = _binding!!

    override fun bindData() {
        try {
            binding.name.text = data.name
            binding.plateNumber.text = data.plateNumber
            binding.itemDescription.text = data.description
            //Carga la foto en el formulario a partir de la URL almacenada
            displayImgURL(data.photoURL, binding.itemImage)

            binding.bar.btclose.setOnClickListener {
                onBtClose(InventoryFragment())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun editDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun delDocument(s: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DetailItemBinding.inflate(inflater, container, false)
        val root: View = binding.root
        bindData()
        return root
    }

}