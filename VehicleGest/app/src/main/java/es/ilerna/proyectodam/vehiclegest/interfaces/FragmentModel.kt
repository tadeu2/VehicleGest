package es.ilerna.proyectodam.vehiclegest.interfaces

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.adapters.EmployeeRecyclerAdapter
import es.ilerna.proyectodam.vehiclegest.databinding.FragmentEmployeesBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity
import es.ilerna.proyectodam.vehiclegest.ui.employees.EmployeeAdder
import es.ilerna.proyectodam.vehiclegest.ui.employees.EmployeeDetail

/**
 * Fragmento de listado de empleados
 */
abstract class FragmentModel : Fragment(), Controller.AdapterListener {

    //Creamos una variable para el adaptador
    private lateinit var recyclerView: RecyclerView
    private lateinit var collectionReference: CollectionReference
    private lateinit var searchView: SearchView

    //Fase de creación del fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = (activity as MainActivity).findViewById(R.id.searchView)
        setSearchViewListeners()
    }

    private fun setSearchViewListeners() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    getAllData()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    getAllData()
                } else {
                    filterData(newText);
                }
                return false
            }
        })

        searchView.setOnCloseListener {
            getAllData()
            false
        }
    }

    abstract fun getAllData()

    abstract fun filterData(searchString: String)

}