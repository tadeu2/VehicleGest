package es.ilerna.proyectodam.vehiclegest.data.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

/**
 * Adaptador de firestore
 * @param query
 */

abstract class FirestoreAdapter<fireStoreViewHolder : RecyclerView.ViewHolder>(
    private val query: Query
) : RecyclerView.Adapter<fireStoreViewHolder>(), EventListener<QuerySnapshot> {

    private var registration: ListenerRegistration? = null

    //Variable de instantaneas de documento
    private val snapshots = ArrayList<DocumentSnapshot>()

    //Iniciar escuchador para ver si se ha agregado un item al query
    open fun startListening() {
        if (registration == null) {
            registration = query.addSnapshotListener(this)
        }
    }

    //Parar escuchador
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
        // Handle errors
        if (exception != null) {
            Log.e("onEvent:error", exception.toString())
            return
        }

        // Dispatch the event
        for (change in documentSnapshots!!.documentChanges) {
            // Snapshot of the changed document
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }
    }

    protected open fun onDocumentAdded(change: DocumentChange) {
        snapshots.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

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

    protected open fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    override fun getItemCount(): Int {
        return snapshots.size
    }

    protected open fun getSnapshot(index: Int): DocumentSnapshot? {
        return snapshots[index]
    }
}