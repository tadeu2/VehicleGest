package es.ilerna.proyectodam.vehiclegest.ui.services

import android.content.ContentValues
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
import es.ilerna.proyectodam.vehiclegest.adapters.ServiceRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentServicesBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.interfaces.FragmentModel

/**
 * Fragmento de listado de servicios
 */
class ServiceFragment : FragmentModel() {

    //Variables de fragmento
    private var fragmentServicesBinding: FragmentServicesBinding? = null
    private val getFragmentServicesBinding
        get() = fragmentServicesBinding ?: throw IllegalStateException("Binding is null")

    /**
     * Fase de creación del fragmento
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {

            //Consulta a firestore db de la colección de vehiculos
            collectionReference = Firebase.firestore.collection("service")

        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
    }

    /**
     *  Fase de creación de la vista
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Pintar el fragment
            fragmentServicesBinding = FragmentServicesBinding.inflate(inflater, container, false)

            //Obtener el recycler y el adaptador
            recyclerAdapter = ServiceRecyclerAdapter(collectionReference, this)
            configRecyclerView(getFragmentServicesBinding.recyclerservices)
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
        return getFragmentServicesBinding.root
    }

    /**
     * Al pulsar el botón flotante se abre el fragmento de creación
     */
    override fun onAddButtonClick() {
        fragmentReplacer(ServiceAdderFragment(), parentFragmentManager)
    }

    /**
     * Al pulsar el botón de editar se abre el fragmento de edición
     * @param documentSnapshot Documento de firestore que se va a editar
     */
    override fun onItemSelected(documentSnapshot: DocumentSnapshot?) {
        fragmentReplacer(ServiceDetail(documentSnapshot!!), parentFragmentManager)
    }

    /**
     * Crea un fragmento de detalle de empleado
     * @param item Documento de la base de datos
     * @return Fragmento de detalle de empleado
     */
    override fun getDetailFragment(item: DocumentSnapshot): Fragment = ServiceDetail(item)

    /**
     * Crea un fragmento de añadir empleado
     * @return Fragmento de añadir empleado
     */
    override fun getAdderFragment(): Fragment = ServiceAdderFragment()

    /**
     * Al destruirse el fragmento se elimina la referencia al binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentServicesBinding = null
    }

    /**
     * Actualiza los datos del adaptador a partir de una lista de documentos
     * @param documentSnapshots Lista de documentos a partir de los que se actualiza el adaptador
     */
    override fun updateRecyclerViewAdapterFromDocumentList(documentSnapshots: ArrayList<DocumentSnapshot>) {
        (recyclerAdapter as ServiceRecyclerAdapter).updateData(documentSnapshots)
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