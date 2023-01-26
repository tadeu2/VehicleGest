package es.ilerna.proyectodam.vehiclegest.ui.alerts

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
import es.ilerna.proyectodam.vehiclegest.adapters.AlertRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentAlertsBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.FragmentModel

/**
 * Fragmento de listado de alertas
 */
class AlertsFragment : FragmentModel() {

    //Enlaza el fragmento al xml
    private var fragmentAlertsBinding: FragmentAlertsBinding? = null
    private val getFragmentAlertsBinding
        get() = fragmentAlertsBinding ?: throw IllegalStateException("Binding error")

    /**
     * Crea un fragmento de detalle de empleado
     * @param item Documento de la base de datos
     * @return Fragmento de detalle de empleado
     */
    override fun getDetailFragment(item: DocumentSnapshot): Fragment = AlertDetail(item)

    /**
     * Crea un fragmento de añadir empleado
     * @return Fragmento de añadir empleado
     */
    override fun getAdderFragment(): Fragment = AlertAdder()

    /**
     * Fase de creación del fragmento
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //Referencia a la base de datos de Firebase
            collectionReference = Firebase.firestore.collection("alert")
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
        }
    }

    /**
     * Fase de creación de la vista
     * @param inflater Inflador de la vista
     * @param container Contenedor de la vista
     *  @param savedInstanceState Estado de la instancia
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //Enlaza el fragmento a el xml y lo infla
            fragmentAlertsBinding = FragmentAlertsBinding.inflate(inflater, container, false)

            //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
            recyclerAdapter = AlertRecyclerAdapter(collectionReference, this)

            //Configura el recycler view con un layout manager y un adaptador
            configRecyclerView(getFragmentAlertsBinding.recycleralerts)

        } catch (exception: Exception) {
            Log.w(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
        return getFragmentAlertsBinding.root
    }

    /**
     * Actualiza los datos del adaptador a partir de una lista de documentos
     * @param documentSnapshots Lista de documentos a partir de los que se actualiza el adaptador
     */
    override fun updateRecyclerViewAdapterFromDocumentList(documentSnapshots: ArrayList<DocumentSnapshot>) {
        (recyclerAdapter as AlertRecyclerAdapter).updateData(documentSnapshots)
    }

    /**
     * Genera una lista de filtros a partir de un string de búsqueda
     * @param searchString String de búsqueda
     * @return Lista de filtros
     */
    override fun generateFilteredItemListFromString(searchString: String): List<Query> {
        val queryDni = collectionReference
            .whereGreaterThanOrEqualTo("dni", searchString)
            .whereLessThanOrEqualTo("dni", searchString + "\uf8ff")
        val querySurname = collectionReference
            .whereGreaterThanOrEqualTo("surname", searchString)
            .whereLessThanOrEqualTo("surname", searchString + "\uf8ff")
        return listOf(queryDni, querySurname)
    }

    /**
     * Al destruir el fragmento se elimina la variable de enlace al xml
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentAlertsBinding = null
    }
}