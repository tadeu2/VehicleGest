package es.ilerna.proyectodam.vehiclegest.ui.inspections

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.adapters.ItvRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentInspectionBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.FragmentModel

/**
 * Fragmento de listado de itv
 */
class ItvFragment : FragmentModel() {

    //Enlaza el fragmento al xml
    private var fragmentInspectionBinding: FragmentInspectionBinding? = null
    private val getFragmentInspectionBinding
        get() = fragmentInspectionBinding ?: throw IllegalStateException("Binding error")

    /**
     * Fase de creación del fragmento
     * @param savedInstanceState Bundle de datos
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {

            //Referencia a la base de datos de Firebase
            dbFirestoreReference = Firebase.firestore.collection("ITV")

        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
    }

    /**
     * Fase de creación de la vista
     * @param inflater  Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Bundle de datos
     * @return Vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        try {
            //Pintar el fragment
            fragmentInspectionBinding =
                FragmentInspectionBinding.inflate(inflater, container, false)
            //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
            recyclerAdapter = ItvRecyclerAdapter(dbFirestoreReference, this)

            //Configura el recycler view con un layout manager y un adaptador
            configRecyclerView(getFragmentInspectionBinding.recycleritv)
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
        return getFragmentInspectionBinding.root
    }

    /**
     * Crea un fragmento de detalle
     * @return Fragmento de detalle de tipo DetailFormModelFragment
     */
    override fun getDetailFragment(): DetailFormModelFragment = ItvDetailFragment()

    /**
     * Al destruir el fragmento se destruye el binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentInspectionBinding = null
    }

    /**
     * Actualiza los datos del adaptador a partir de una lista de documentos
     * @param documentSnapshots Lista de documentos a partir de los que se actualiza el adaptador
     */
    override fun updateRecyclerViewAdapterFromDocumentList(documentSnapshots: ArrayList<DocumentSnapshot>) {
        (recyclerAdapter as ItvRecyclerAdapter).updateData(documentSnapshots)
    }

    /**
     * Genera una lista de filtros a partir de un string de búsqueda
     * @param searchString String de búsqueda
     * @return Lista de filtros
     */
    override fun generateFilteredItemListFromString(searchString: String): List<Query> {
        TODO("PARA BUSCAR POR FECHA")
    }
}