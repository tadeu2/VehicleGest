package es.ilerna.proyectodam.vehiclegest.ui.inventory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.adapters.ItemRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentInventoryBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.FragmentModel
import es.ilerna.proyectodam.vehiclegest.ui.vehicles.VehiclesFragment

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
    override fun getDetailFragment(): DetailFormModelFragment  = ItemDetailFragment()

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

            //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
            recyclerAdapter = ItemRecyclerAdapter(dbFirestoreReference, this)

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

    /**
     * Actualiza los datos del adaptador a partir de una lista de documentos
     * @param documentSnapshots Lista de documentos a partir de los que se actualiza el adaptador
     */
    override fun updateRecyclerViewAdapterFromDocumentList(documentSnapshots: ArrayList<DocumentSnapshot>) {
        (recyclerAdapter as ItemRecyclerAdapter).updateData(documentSnapshots)
    }

    /**
     * Genera una lista de filtros a partir de un string de búsqueda
     * @param searchString String de búsqueda
     * @return Lista de filtros
     */
    override fun generateFilteredItemListFromString(searchString: String): List<Query> {
        val queryPlateNumber = dbFirestoreReference
            .whereGreaterThanOrEqualTo("platenumber", searchString)
            .whereLessThanOrEqualTo("platenumber", searchString + "\uf8ff")
        return listOf(queryPlateNumber)
    }
}