package es.ilerna.proyectodam.vehiclegest.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Clase que representa a una entidad de la base de datos Firestore
 * @param plateNumber Matrícula del vehículo
 * @param description Descripción del item
 * @param photoURL URL de la foto del item
 * @parm name Nombre del item
 */
@IgnoreExtraProperties
data class Item(
    var plateNumber: String? = null,
    var name: String? = null,
    var description: String? = null,
    var photoURL: String? = null
) : Parcelable {

    /**
     * Constructor privado para la creación de objetos Item a partir de un Parcel
     * @param parcel Parcel que contiene los datos del item
     */
    private constructor(parcel: Parcel) : this() {
        plateNumber = parcel.readString()
        name = parcel.readString()
        description = parcel.readString()
        photoURL = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    /**
     * Método que escribe los datos del objeto Item en un Parcel
     * @param parcel Parcel donde se escribirán los datos del item
     * @param flags Flags
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(plateNumber)
        dest.writeString(name)
        dest.writeString(description)
        dest.writeString(photoURL)
    }

    /**
     *  Objeto que implementa la interfaz Parcelable.Creator para la creación de objetos Item
     */
    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        /**
         * Método que crea un array de objetos Item
         * @param size Tamaño del array
         * @return Array de objetos Item
         */
        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}
