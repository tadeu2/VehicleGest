package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

/**
 * Interfaz para crear escuchadores para las diferentes entidades de la base de datos Firestore
 */
abstract class DetailFragmentAdapter(
    query: Query,
    private val listener: EventListener<QuerySnapshot>
) : FirestoreAdapter<RecyclerView.ViewHolder>(query) {

    lateinit var navBarTop: MaterialToolbar
    lateinit var navBarBot: BottomNavigationView


    abstract class EntityViewHolder(
        val view: View
    ) : RecyclerView.ViewHolder(view) {


        abstract fun bindData(snapshot: DocumentSnapshot, listener: EventListener<QuerySnapshot>)

        /**
         * Rellena cada item de la tarjeta con los datos del objeto vehiculo
         * @param vehicle Ficha de cada vehículo
         */

    }

}
