package es.ilerna.proyectodam.vehiclegest.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

/**
 * Clase que representa a una entidad de la base de datos Firestore
 * @param plateNumber Matrícula del vehículo
 * @param description Descripción de la alerta
 * @param date Fecha de la alerta
 * @para solved Indica si la alerta ha sido resuelta
 */
@IgnoreExtraProperties //Annotación para ignorar propiedades no utilizadas
data class Alert(
    var plateNumber: String? = null,
    var date: Date? = null,
    var description: String? = null,
    var solved: Boolean? = false
) : Parcelable {

    /**
     * Constructor privado para la creación de objetos Alert a partir de un Parcel
     * @param parcel Parcel que contiene los datos de la alerta
     */
    private constructor(parcel: Parcel) : this() {
        plateNumber = parcel.readString()
        date = Timestamp(parcel.readLong(), 0).toDate()
        description = parcel.readString()
        solved = parcel.readBoolean()
    }

    /**
     * Método que describe el contenido del objeto Alert
     * @return 0
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * Método que escribe los datos del objeto Alert en un Parcel
     * @param dest Parcel donde se escribirán los datos
     * @param flags Indica si se escriben los datos en el Parcel
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(plateNumber)
        dest.writeString(description)
        date?.time?.let { dest.writeLong(it) }
        solved?.let { dest.writeBoolean(it) }
    }

    /**
     *  Objeto que implementa la interfaz Parcelable.Creator para la creación de objetos Alert
     */
    companion object CREATOR : Parcelable.Creator<Alert> {
        override fun createFromParcel(parcel: Parcel): Alert {
            return Alert(parcel)
        }

        /**
         * Método que crea un array de objetos Alert
         * @param size Tamaño del array
         */
        override fun newArray(size: Int): Array<Alert?> {
            return arrayOfNulls(size)
        }
    }
}
