package es.ilerna.proyectodam.vehiclegest.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Service(
    var plateNumber: String? = null,
    var date: Date? = null,
    var remarks: String? = null,
    var costumer: String? = null
) : Parcelable {


    private constructor(parcel: Parcel) : this() {
        plateNumber = parcel.readString()
        date = Timestamp(parcel.readLong(), 0).toDate()
        remarks = parcel.readString()
        costumer = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(plateNumber)
        dest.writeString(remarks)
        date?.time?.let { dest.writeLong(it) }
        dest.writeString(costumer)
    }

    companion object CREATOR : Parcelable.Creator<Service> {
        override fun createFromParcel(parcel: Parcel): Service {
            return Service(parcel)
        }

        override fun newArray(size: Int): Array<Service?> {
            return arrayOfNulls(size)
        }
    }
}
