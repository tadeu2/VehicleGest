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
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.entities.Item
import es.ilerna.proyectodam.vehiclegest.databinding.DetailItemBinding
import java.net.URL
import java.util.concurrent.Executors


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class ItemDetail(val data: Item) : Fragment() {

    private var _binding: DetailItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var navBarTop: MaterialToolbar
    private lateinit var navBarBot: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        //Esconde barras de navegación
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarTop.visibility = GONE
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = GONE

        _binding = DetailItemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.name.text = data.name
        binding.plateNumber.text = data.plateNumber
        binding.itemDescription.text = data.description

        this.displayImgURL(data.photoURL, binding.itemImage)
        //Foto del vehículo
        //Glide.with(binding.root).load(data.photoURL).into(binding.itemImage)

        binding.bar.btclose.setOnClickListener {
            this.onBtClose()
        }

        return root

    }

    private fun onBtClose() {
        navBarBot.visibility = VISIBLE
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, InventoryFragment())
        fragmentTransaction.commit()
    }

    /**
     * Ejecuta un hilo paralelo para asignar una imagen URL al campo de imagen
     */
    private fun displayImgURL(url: String?, imgView: ShapeableImageView?) {

        try {
            val u = URL(url)

            // Declaramos un ejecutor para el nuevo hilo
            val executor = Executors.newSingleThreadExecutor()
            //Declaramos un manejador que asigne la imagen al objecto imagen
            val handler = Handler(Looper.getMainLooper())

            //Creamos el objecto imagen vacio y le asignamos por stream a otra variable
            var image: Bitmap? = null
            val im = u.openStream()
            image = BitmapFactory.decodeStream(im)

            //Para hacer cambios en la interfaz
            handler.post {
                imgView?.setImageBitmap(image)
            }
        } catch (e: Exception) {
            //TODO Lanzar mensaje de error en popup
            e.printStackTrace()
        }

    }

}