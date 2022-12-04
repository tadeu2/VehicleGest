package es.ilerna.proyectodam.vehiclegest.backend

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.firestore.DocumentSnapshot
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import java.net.URL
import java.util.concurrent.Executors

/**
 * Interfaz para crear escuchadores para las diferentes entidades de la base de datos Firestore
 */
abstract class DetailFragment : Fragment() {

    lateinit var navBarTop: MaterialToolbar
    lateinit var navBarBot: BottomNavigationView
    lateinit var floatingButton: FloatingActionButton

    abstract fun bindData() //Enlazar datos al formulario de texto
    abstract fun editDocument(s: DocumentSnapshot)
    abstract fun delDocument(s: DocumentSnapshot)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Esconde barras de navegación
        navBarTop = requireActivity().findViewById(R.id.topToolbar)
        navBarTop.visibility = GONE
        navBarBot = requireActivity().findViewById(R.id.bottom_nav_menu)
        navBarBot.visibility = GONE
        floatingButton = requireActivity().findViewById(R.id.addButton)
        floatingButton.visibility = GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        //La barra superior vuelve a ser visible al destruirse el fragmento
        navBarTop.visibility = VISIBLE
        navBarBot.visibility = VISIBLE
        floatingButton.visibility = VISIBLE
    }

    open fun onBtClose(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment)
        fragmentTransaction.commit()
    }

    /**
     * Ejecuta un hilo paralelo para asignar una imagen URL al campo de imagen
     */
    open fun displayImgURL(url: String?, imgView: ShapeableImageView?) {

        try {
            //Crea un hilo paralelo para descargar las imagenes de una URL
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                val u = URL(url)
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
            }
        } catch (e: Exception) {
            //TODO Lanzar mensaje de error en popup
            e.printStackTrace()
        }
    }
}
