package es.ilerna.proyectodam.vehiclegest.data.entities

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class Service : Parcelable {

    var plateNumber: String? = null
    var serviceDate: Date? = null
    var remarks: String? = null
    var costumer: String? = null

    constructor()

    @RequiresApi(Build.VERSION_CODES.Q)
    private constructor(parcel: Parcel) {
        plateNumber = parcel.readString()
        serviceDate = Timestamp(parcel.readLong(), 0).toDate()
        remarks = parcel.readString()
        costumer = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(plateNumber)
        dest.writeString(remarks)
        serviceDate?.time?.let { dest.writeLong(it) }
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
