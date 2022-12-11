package es.ilerna.proyectodam.vehiclegest.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

/**
 * Escribe y lee los datos de las entidades vehículo
 */
@IgnoreExtraProperties
data class Vehicle(
    var plateNumber: String? = null,
    var type: String? = null,
    var brand: String? = null,
    var model: String? = null,
    var expiryDateITV: Date? = null,
    var totalDistance: Int? = 0,
    var licensed: Boolean? = null,
    var description: String? = null,
    var photoURL: String? = null
) : Parcelable {

    @RequiresApi(Build.VERSION_CODES.Q)
    private constructor(parcel: Parcel) : this() {
        plateNumber = parcel.readString()
        type = parcel.readString()
        brand = parcel.readString()
        model = parcel.readString()
        expiryDateITV = Timestamp(parcel.readLong(), 0).toDate()
        totalDistance = parcel.readInt()
        licensed = parcel.readBoolean()
        description = parcel.readString()
        photoURL = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(plateNumber)
        dest.writeString(type)
        dest.writeString(brand)
        dest.writeString(model)
        expiryDateITV?.time?.let { dest.writeLong(it) }
        totalDistance?.let { dest.writeInt(it) }
        licensed?.let { dest.writeBoolean(it) }
        dest.writeString(description)
        dest.writeString(photoURL)
    }

    companion object CREATOR : Parcelable.Creator<Vehicle> {
        override fun createFromParcel(parcel: Parcel): Vehicle {
            return Vehicle(parcel)
        }

        override fun newArray(size: Int): Array<Vehicle?> {
            return arrayOfNulls(size)
        }
    }
}
