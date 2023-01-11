package es.ilerna.proyectodam.vehiclegest.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

/**
 * Clase que representa a una entidad de la base de datos Firestore
 * @para date: Date? (Fecha de la ITV)
 */
@IgnoreExtraProperties
data class ITV(
    var date: Date? = null,
    var remarks: String? = null
) : Parcelable {

    /**
     * Constructor privado para la creación de objetos ITV a partir de un Parcel
     * @param parcel Parcel que contiene los datos de la ITV
     */
    private constructor(parcel: Parcel) : this() {
        date = Timestamp(parcel.readLong(), 0).toDate()
        remarks = parcel.readString()
    }


    /**
     * Método que describe el contenido del objeto ITV
     * @return 0
     */
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        date?.time?.let { dest.writeLong(it) }
        dest.writeString(remarks)
    }

    /**
     * Objeto que implementa la interfaz Parcelable.Creator para la creación de objetos ITV
     */
    companion object CREATOR : Parcelable.Creator<ITV> {
        override fun createFromParcel(parcel: Parcel): ITV {
            return ITV(parcel)
        }

        /**
         * Método que crea un array de objetos ITV
         * @param size Tamaño del array a crear
         * @return Array de objetos ITV
         */
        override fun newArray(size: Int): Array<ITV?> {
            return arrayOfNulls(size)
        }
    }
}
