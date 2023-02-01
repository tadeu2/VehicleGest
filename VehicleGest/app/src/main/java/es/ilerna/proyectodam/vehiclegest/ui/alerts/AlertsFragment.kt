package es.ilerna.proyectodam.vehiclegest.ui.alerts

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.adapters.AlertRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentAlertsBinding
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailFormModelFragment
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
     * Crea un fragmento de detalle
     * @return Fragmento de detalle de tipo DetailFormModelFragment
     */
    override fun getDetailFragment(): DetailFormModelFragment = AlertDetailFragment()

    /**
     * Fase de creación del fragmento
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //Referencia a la base de datos de Firebase
            dbFirestoreReference = Firebase.firestore.collection("alert")
            searchStringList = listOf("date") // Lista de campos por los que se puede buscar
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
            recyclerAdapter = AlertRecyclerAdapter(dbFirestoreReference, this)

            //Configura el recycler view con un layout manager y un adaptador
            configRecyclerView(getFragmentAlertsBinding.recycleralerts)

        } catch (exception: Exception) {
            Log.w(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
        return getFragmentAlertsBinding.root
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