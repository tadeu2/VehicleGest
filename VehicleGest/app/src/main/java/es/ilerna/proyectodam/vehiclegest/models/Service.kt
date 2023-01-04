package es.ilerna.proyectodam.vehiclegest.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

/**
 * Clase que representa a una entidad de la base de datos Firestore
 * @param plateNumber Matrícula del vehículo
 * @param date: Date? (Fecha del servicio)
 * @param remarks String? (Observaciones del servicio)
 * @param costumer String? (Cliente del servicio)
 */
@IgnoreExtraProperties
data class Service(
    var plateNumber: String? = null,
    var date: Date? = null,
    var remarks: String? = null,
    var costumer: String? = null
) : Parcelable {

    /**
     * Constructor privado para la creación de objetos Service a partir de un Parcel
     * @param parcel Parcel que contiene los datos del servicio
     */
    private constructor(parcel: Parcel) : this() {
        plateNumber = parcel.readString()
        date = Timestamp(parcel.readLong(), 0).toDate()
        remarks = parcel.readString()
        costumer = parcel.readString()
    }

    /**
     * Método que describe el contenido del objeto Service
     * @return 0
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * Método que escribe los datos del objeto Service en un Parcel
     * @param dest Parcel donde se escribirán los datos del objeto Service
     * @param flags Int que indica el modo de escritura
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(plateNumber)
        dest.writeString(remarks)
        date?.time?.let { dest.writeLong(it) }
        dest.writeString(costumer)
    }

    /**
     *  Objeto que implementa la interfaz Parcelable.Creator para la creación de objetos Service
     */
    companion object CREATOR : Parcelable.Creator<Service> {

        /**
         * Método que crea un objeto Service a partir de un Parcel
         * @param parcel Parcel que contiene los datos del objeto Service
         * @return Objeto Service
         */
        override fun createFromParcel(parcel: Parcel): Service {
            return Service(parcel)
        }

        /**
         * Método que crea un array de objetos Service
         * @param size Tamaño del array a crear
         * @return Array de objetos Service
         */
        override fun newArray(size: Int): Array<Service?> {
            return arrayOfNulls(size)
        }
    }
}
