package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.adapters.VehicleRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentVehiclesBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.FragmentModel
import java.util.ArrayList

/**
 * Fragmento de listado de vehículos
 */
class VehiclesFragment : FragmentModel(){

    //Enlaza el fragmento con el xml
    private var fragmentVehiclesBinding: FragmentVehiclesBinding? = null
    private val getFragmentVehiclesBinding
        get() = fragmentVehiclesBinding ?: throw IllegalStateException("Binding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //Referencia a la base de datos de Firebase
            collectionReference = Firebase.firestore.collection("vehicle")

        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
        }
    }

    /**
     * Fase de creación de la vista
     * @param inflater Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Estado de la instancia
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Enlaza el fragmento a el xml y lo infla
        fragmentVehiclesBinding = FragmentVehiclesBinding.inflate(inflater, container, false)

        //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
        recyclerAdapter = VehicleRecyclerAdapter(collectionReference, this)

        configRecyclerView(getFragmentVehiclesBinding.recyclerVehicles)
        return getFragmentVehiclesBinding.root
    }

    /**
     * Al seleccionar un elemento del recycler se abre el formulario de edición
     * @param documentSnapshot Documento seleccionado
     */
    override fun onItemSelected(documentSnapshot: DocumentSnapshot?) {
        fragmentReplacer(VehicleDetail(documentSnapshot!!), parentFragmentManager)
    }

    /**
     * Al pulsar el botón flotante se abre el formulario de creación
     */
    override fun onAddButtonClick() {
        fragmentReplacer(VehicleAdder(), parentFragmentManager)
    }

    /**
     * Crea un fragmento de detalle de empleado
     * @param item Documento de la base de datos
     * @return Fragmento de detalle de empleado
     */
    override fun getDetailFragment(item: DocumentSnapshot): Fragment = VehicleDetail(item)

    /**
     * Crea un fragmento de añadir empleado
     * @return Fragmento de añadir empleado
     */
    override fun getAdderFragment(): Fragment = VehicleAdder()

    /**
     * Al destruir la vista, elimina el enlace con el xml
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentVehiclesBinding = null
    }

    /**
     * Actualiza los datos del adaptador a partir de una lista de documentos
     * @param documentSnapshots Lista de documentos a partir de los que se actualiza el adaptador
     */
    override fun updateRecyclerViewAdapterFromDocumentList(documentSnapshots: ArrayList<DocumentSnapshot>) {
        (recyclerAdapter as VehicleRecyclerAdapter).updateData(documentSnapshots)
    }

    /**
     * Genera una lista de filtros a partir de un string de búsqueda
     * @param searchString String de búsqueda
     * @return Lista de filtros
     */
    override fun generateFilteredItemListFromString(searchString: String): List<Query> {
        val queryplateNumber = collectionReference
            .whereGreaterThanOrEqualTo("platenumber", searchString)
            .whereLessThanOrEqualTo("platenumber", searchString + "\uf8ff")
        return listOf(queryplateNumber)
    }
}

