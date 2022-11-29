package es.ilerna.proyectodam.vehiclegest.data.entities

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class Employee : Parcelable {

    var dni : String? = null
    var name: String? = null
    var surname: String? = null
    var address: String? = null
    var email: String? = null
    var phone: String? = null
    var birthdate: Date? = null
    var isAdmin: Boolean? = false

    constructor()

    @RequiresApi(Build.VERSION_CODES.Q)
    private constructor(parcel: Parcel) {
        dni = parcel.readString()
        name = parcel.readString()
        surname = parcel.readString()
        address = parcel.readString()
        email = parcel.readString()
        phone = parcel.readString()
        birthdate = Timestamp(parcel.readLong(), 0).toDate()
        isAdmin = parcel.readBoolean()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(dni)
        dest.writeString(name)
        dest.writeString(surname)
        dest.writeString(address)
        dest.writeString(email)
        birthdate?.time?.let { dest.writeLong(it) }
       isAdmin?.let { dest.writeBoolean(it) }
    }

    companion object CREATOR : Parcelable.Creator<Employee> {
        override fun createFromParcel(parcel: Parcel): Employee {
            return Employee(parcel)
        }

        override fun newArray(size: Int): Array<Employee?> {
            return arrayOfNulls(size)
        }
    }
}
