package es.ilerna.proyectodam.vehiclegest.ui.inspections

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            searchStringList = listOf("date")//Lista de campos de busqueda

            //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
            recyclerAdapter = ItvRecyclerAdapter(dbFirestoreReference, this)

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

}