package es.ilerna.proyectodam.vehiclegest.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

/**
 * Clase abstracta
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 * @param query Parámetro que contiene la consulta a la base de datos
 */

abstract class FirestoreAdapter<fireStoreViewHolder : RecyclerView.ViewHolder>(
    private val query: Query
) : RecyclerView.Adapter<fireStoreViewHolder>(), EventListener<QuerySnapshot> {
    //Listener de la consulta a la base de datos
    private var registration: ListenerRegistration? = null

    //Variable de instantaneas de documento
    private val snapshots = ArrayList<DocumentSnapshot>()

    //Iniciar escuchador para ver si se ha agregado un item al query
    open fun startListening() {
        if (registration == null) {
            registration = query.addSnapshotListener(this)
        }
    }

    //Detener escuchador para ver si se ha agregado un item al query
    open fun stopListening() {
        if (registration != null) {
            registration!!.remove()
            registration = null
        }

        snapshots.clear()
        notifyDataSetChanged()
    }

    override fun onEvent(
        documentSnapshots: QuerySnapshot?,
        exception: FirebaseFirestoreException?
    ) {
        //Si hay un error, lo muestra en el log
        if (exception != null) {
            Log.e("onEvent:error", exception.toString())
            return
        }

        //Si no hay error, actualiza los datos
        for (change in documentSnapshots!!.documentChanges) {
            //
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }
    }

    //Añade un item al query
    protected open fun onDocumentAdded(change: DocumentChange) {
        snapshots.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    //Modifica un item del query
    protected open fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            // Item modidicado pero se queda en el mismo lugar
            snapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            // Item modidicado pero cambia el lugar
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    //Elimina un item del query
    protected open fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    //Devuelve el número de items del query
    override fun getItemCount(): Int {
        return snapshots.size
    }

    //Devuelve el id del item
    protected open fun getSnapshot(index: Int): DocumentSnapshot? {
        return snapshots[index]
    }
}