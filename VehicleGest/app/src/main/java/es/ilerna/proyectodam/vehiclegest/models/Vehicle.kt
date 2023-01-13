package es.ilerna.proyectodam.vehiclegest.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

/**
 * Escribe y lee los datos de las entidades vehículo
 *  @param plateNumber Matrícula del vehículo
 *  @param brand Marca del vehículo
 *  @param model Modelo del vehículo
 *  @param type Tipo de vehículo
 *  @param expiryDateITV Fecha de caducidad de la ITV
 *  @param totalDistance Distancia total recorrida
 *  @param itvPassed ITV pasada
 *  @param description Descripción del vehículo
 *  @param photoURL URL de la foto del vehículo
 */
@IgnoreExtraProperties
data class Vehicle(
    var plateNumber: String? = null,
    var type: String? = null,
    var brand: String? = null,
    var model: String? = null,
    var expiryDateITV: Date? = null,
    var totalDistance: Int? = 0,
    var itvPassed: Boolean? = null,
    var description: String? = null,
    var photoURL: String? = null
) : Parcelable {

    /**
     * Constructor privado para la creación de objetos Vehicle a partir de un Parcel
     * @param parcel Parcel que contiene los datos del vehículo
     */
    private constructor(parcel: Parcel) : this() {
        plateNumber = parcel.readString()
        type = parcel.readString()
        brand = parcel.readString()
        model = parcel.readString()
        expiryDateITV = Timestamp(parcel.readLong(), 0).toDate()
        totalDistance = parcel.readInt()
        itvPassed = parcel.readBoolean()
        description = parcel.readString()
        photoURL = parcel.readString()
    }

    /**
     * Método que describe el contenido del objeto Vehicle
     * @return 0
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * Método que escribe los datos del objeto Vehicle en un Parcel
     * @param dest Parcel donde se escribirán los datos del objeto Vehicle
     * @param flags Int que indica el modo de escritura
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(plateNumber)
        dest.writeString(type)
        dest.writeString(brand)
        dest.writeString(model)
        expiryDateITV?.time?.let { dest.writeLong(it) }
        totalDistance?.let { dest.writeInt(it) }
        itvPassed?.let { dest.writeBoolean(it) }
        dest.writeString(description)
        dest.writeString(photoURL)
    }

    /**
     *  Objeto que implementa la interfaz Parcelable.Creator para la creación de objetos Vehicle
     */
    companion object CREATOR : Parcelable.Creator<Vehicle> {

        /**
         * Método que crea un objeto Vehicle a partir de un Parcel
         * @param parcel Parcel que contiene los datos del vehículo
         * @return Objeto Vehicle creado
         */
        override fun createFromParcel(parcel: Parcel): Vehicle {
            return Vehicle(parcel)
        }

        /**
         * Método que crea un array de objetos Vehicle
         * @param size Tamaño del array
         * @return Array de objetos Vehicle
         */
        override fun newArray(size: Int): Array<Vehicle?> {
            return arrayOfNulls(size)
        }
    }
}
