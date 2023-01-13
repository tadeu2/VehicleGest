package es.ilerna.proyectodam.vehiclegest.ui.inspections

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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.adapters.ItvRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentInspectionBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer

/**
 * Fragmento de listado de itv
 */
class ItvFragment : Fragment(), Controller.AdapterListener {

    //Enlaza el fragmento al xml
    private var fragmentInspectionBinding: FragmentInspectionBinding? = null
    private val getFragmentInspectionBinding
        get() = fragmentInspectionBinding ?: throw IllegalStateException("Binding error")

    //Crea una variable para el adaptador
    private lateinit var itvRecyclerAdapter: ItvRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var itvCollectionReference: CollectionReference //Consulta de firestore

    /**
     * Fase de creación del fragmento
     * @param savedInstanceState Bundle de datos
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //Referencia a la base de datos de Firebase
            itvCollectionReference = Firebase.firestore.collection("ITV")
            //Crea un escuchador para el botón flotante que abre el formulario de creacion
            activity?.findViewById<FloatingActionButton>(R.id.addButton)
                ?.setOnClickListener {
                    onAddButtonClick()
                }
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

            //Pintar el recyclerview
            //Enlaza el recycler a la variable
            recyclerView = getFragmentInspectionBinding.recycleritv
            //Le asigna un manager lineal en el contexto de este fragmento
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true) //Para que no se recargue el tamaño del recycler
            //Crea una instancia del recycleradapter, con la consulta y le asigna el escuchador a este fragmento
            itvRecyclerAdapter = ItvRecyclerAdapter(itvCollectionReference, this)
            recyclerView.adapter = itvRecyclerAdapter //Asigna el adaptador al recycler
        } catch (exception: Exception) {
            Log.e(ContentValues.TAG, exception.message.toString(), exception)
            exception.printStackTrace()
        }
        return getFragmentInspectionBinding.root
    }

    /**
     * Al seleccionar un item de la lista se abre el fragmento de detalle
     * @param documentSnapshot Documento de firestore
     */
    override fun onItemSelected(documentSnapshot: DocumentSnapshot?) {
        fragmentReplacer(ItvDetail(documentSnapshot!!), parentFragmentManager)
    }

    /**
     * Al pulsar el botón flotante se abre el fragmento de creación
     */
    override fun onAddButtonClick() {
        fragmentReplacer(ItvAdder(), parentFragmentManager)
    }

    /**
     * Al iniciar el fragmento se inicia el escuchador del adapter
     */
    override fun onStart() {
        super.onStart()
        itvRecyclerAdapter.startListening()
    }

    /**
     * Al parar el fragmento se para el escuchador del adapter
     */
    override fun onStop() {
        super.onStop()
        itvRecyclerAdapter.stopListening()
    }

    /**
     * Al destruir el fragmento se destruye el binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //Vaciamos la variable de enlace al xml
        fragmentInspectionBinding = null
    }
}