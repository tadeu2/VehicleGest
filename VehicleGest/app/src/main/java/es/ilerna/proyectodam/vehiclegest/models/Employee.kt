package es.ilerna.proyectodam.vehiclegest.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

/**
 * Clase que representa a una entidad de la base de datos Firestore
 * @param dni: String? (DNI del empleado)
 * @param name: String? (Nombre del empleado)
 * @param surname: String? (Apellidos del empleado)
 * @param email: String? (Email del empleado)
 * @param photoURL: String? (URL de la foto del empleado)
 * @param phone: String? (Teléfono del empleado)
 * @param address: String? (Dirección del empleado)
 * @param birthdate: Date? (Fecha de nacimiento del empleado)
 * @para admin: Boolean? (Indica si el empleado es administrador)
 */
@IgnoreExtraProperties
data class Employee(
    var dni: String? = null,
    var name: String? = null,
    var surname: String? = null,
    var address: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var birthdate: Date? = null,
    var photoURL: String? = null,
    var admin: Boolean? = null
) : Parcelable {

    /**
     * Constructor privado para la creación de objetos Employee a partir de un Parcel
     * @param parcel Parcel que contiene los datos del empleado
     */
    private constructor(parcel: Parcel) : this() {
        dni = parcel.readString()
        name = parcel.readString()
        surname = parcel.readString()
        address = parcel.readString()
        email = parcel.readString()
        phone = parcel.readString()
        birthdate = Timestamp(parcel.readLong(), 0).toDate()
        photoURL = parcel.readString()
        admin = parcel.readByte() != 0.toByte()
    }

    /**
     * Método que describe el contenido del objeto Employee
     * @return 0
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * Método que escribe los datos del objeto Employee en un Parcel
     * @param dest Parcel donde se escribirán los datos
     * @param flags Indica si se escriben los datos en el Parcel
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(dni)
        dest.writeString(name)
        dest.writeString(surname)
        dest.writeString(address)
        dest.writeString(email)
        dest.writeString(phone)
        birthdate?.time?.let { dest.writeLong(it) }
        dest.writeString(photoURL)
        dest.writeByte(if (admin == true) 1 else 0)
    }

    /**
     *  Objeto que implementa la interfaz Parcelable.Creator para la creación de objetos Employee
     */
    companion object CREATOR : Parcelable.Creator<Employee> {
        override fun createFromParcel(parcel: Parcel): Employee {
            return Employee(parcel)
        }

        /**
         * Método que crea un array de objetos Employee
         * @param size Tamaño del array
         * @return Array de objetos Employee
         */
        override fun newArray(size: Int): Array<Employee?> {
            return arrayOfNulls(size)
        }
    }
}
