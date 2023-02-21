package es.ilerna.proyectodam.vehiclegest.ui.inventory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.adapters.ItemRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentInventoryBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.FragmentModel

/**
 * Fragmento de listado de inventario
 */
class InventoryFragment : FragmentModel() {

    //Inicializamos el binding con el XML de la interfaz
    private var fragmentInventoryBinding: FragmentInventoryBinding? = null
    private val getfragmentInventoryBinding
        get() = fragmentInventoryBinding ?: throw IllegalStateException("Binding error")

    /**
     * Crea un fragmento de detalle
     * @return Fragmento de detalle de tipo DetailFormModelFragment
     */
    override fun getDetailFragment(): DetailFormModelFragment = ItemDetailFragment()

    /**
     * Fase de creación del fragmento
     * @param savedInstanceState Bundle de datos
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {

            //Referencia a la base de datos de Firebase
            dbFirestoreReference = Firebase.firestore.collection("inventory")
            //Crea un escuchador para el botón flotante que abre el formulario de creacion
            searchStringList = listOf("name", "platenumber")

            //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
            recyclerAdapter = ItemRecyclerAdapter(dbFirestoreReference, this)


        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    /**
     *  Fase de creación de la vista
     *  @param inflater  Inflador de la vista
     *  @param container Contenedor de la vista
     *  @param savedInstanceState Bundle de datos
     *  @return Vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Pintar el fragment
            fragmentInventoryBinding = FragmentInventoryBinding.inflate(inflater, container, false)

            //Configura el recyclerview
            configRecyclerView(getfragmentInventoryBinding.recycleritems)
        } catch (exception: Exception) {
            Log.e("InventoryFragment", exception.message.toString(), exception)
            exception.printStackTrace()
        }
        return getfragmentInventoryBinding.root
    }

    /**
     * Al destruir el fragmento, se destruye el binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentInventoryBinding = null

    }
}