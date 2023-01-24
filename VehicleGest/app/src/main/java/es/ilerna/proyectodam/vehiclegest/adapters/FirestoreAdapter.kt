package es.ilerna.proyectodam.vehiclegest.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import es.ilerna.proyectodam.vehiclegest.models.Vehicle

/**
 * Clase abstracta
 * El adapter se encarga de meter los datos en el recyclerview
 * y de actualizarlo cuando cambien los datos en la base de datos
 * Implementa a RecyclerView.Adapter y a EventListener
 * @param <T> Tipo de datos que se van a mostrar en el recyclerview <ViewHolder>
 * @param queryFirestoreDatabase Parámetro que contiene la consulta a la base de datos
 *
 */

abstract class FirestoreAdapter<fireStoreViewHolder : RecyclerView.ViewHolder>(
    private var queryFirestoreDatabase: Query
) : RecyclerView.Adapter<fireStoreViewHolder>(), EventListener<QuerySnapshot> {
    //Listener de la consulta a la base de datos
    private var listenerRegistration: ListenerRegistration? = null

    //Lista de instantaneas de documento
    private var documentSnapshotArrayList = ArrayList<DocumentSnapshot>()

    /**
     * Función que inicializa el listener de la consulta a la base de datos
     */
    open fun startListening() {
        if (listenerRegistration == null) {
            listenerRegistration = queryFirestoreDatabase.addSnapshotListener(this)
        }
    }

    /**
     * Función que detiene el listener de la consulta a la base de datos
     */
    open fun stopListening() {
        if (listenerRegistration != null) {
            listenerRegistration!!.remove()
            listenerRegistration = null
        }
        //Vaciamos la lista de instantaneas de documento
        documentSnapshotArrayList.clear()
        //Notificamos al adapter que los datos han cambiado y que debe actualizar el recyclerview
        notifyDataSetChanged()
    }

    /**
     *  Función de evento que se ejecuta cuando cambian los datos en la base de datos
     *  @param documentSnapshots Instantaneas de documento que se han modificado
     *  @param firestoreException Excepción que se ha producido en la base de datos
     */
    override fun onEvent(
        documentSnapshots: QuerySnapshot?,
        firestoreException: FirebaseFirestoreException?
    ) {
        //Si hay un error, lo muestra en el log y sale de la función
        if (firestoreException != null) {
            Log.e("onEvent:error", firestoreException.toString())
            return
        }
        //Si no hay error, actualiza los datos del recyclerview y notifica al adapter que los datos han cambiado
        for (change in documentSnapshots!!.documentChanges) {
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }
    }

    /**
     * Función que se ejecuta cuando se añade un documento a la base de datos
     * @param change Cambio que se ha producido en la base de datos
     */
    protected open fun onDocumentAdded(change: DocumentChange) {
        //Añade la instantanea de documento a la lista de instantaneas de documento
        documentSnapshotArrayList.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    /**
     * Función que se ejecuta cuando se modifica un documento de la base de datos
     * @param change Cambio que se ha producido en la base de datos
     */
    protected open fun onDocumentModified(change: DocumentChange) {
        //Si el documento modificado no está en la lista de instantaneas de documento, lo añade
        if (change.oldIndex == change.newIndex) {
            // Item modidicado pero se queda en el mismo lugar
            documentSnapshotArrayList[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            // Item modificado y se mueve de lugar
            documentSnapshotArrayList.removeAt(change.oldIndex)
            documentSnapshotArrayList.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    /**
     * Función que se ejecuta cuando se elimina un documento de la base de datos
     * @param change Cambio que se ha producido en la base de datos
     */
    protected open fun onDocumentRemoved(change: DocumentChange) {
        //Elimina la instantanea de documento de la lista de instantaneas de documento
        documentSnapshotArrayList.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    /**
     * Función que devuelve el número de documentos de la base de datos
     * @return Número de documentos de la base de datos
     */
    override fun getItemCount(): Int {
        return documentSnapshotArrayList.size
    }

    /**
     * Función que devuelve la instantanea de documento de la posición indicada
     * @param position Posición de la instantanea de documento que se quiere obtener
     * @return Instantanea de documento de la posición indicada
     */
    protected open fun getSnapshot(position: Int): DocumentSnapshot? {
        return documentSnapshotArrayList[position]
    }

    fun updateData(query:ArrayList<DocumentSnapshot>) {
        documentSnapshotArrayList = query
        notifyDataSetChanged()
    }
}