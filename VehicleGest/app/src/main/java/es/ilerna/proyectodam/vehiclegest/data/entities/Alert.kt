package es.ilerna.proyectodam.vehiclegest.data.entities

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class Alert : Parcelable {

    var plateNumber: String? = null
    var date: Date? = null
    var description: String? = null
    var solved: Boolean? = false

    constructor()

    @RequiresApi(Build.VERSION_CODES.Q)
    private constructor(parcel: Parcel) {
        plateNumber = parcel.readString()
        date = Timestamp(parcel.readLong(), 0).toDate()
        description = parcel.readString()
        solved = parcel.readBoolean()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(plateNumber)
        dest.writeString(description)
        date?.time?.let { dest.writeLong(it) }
        solved?.let { dest.writeBoolean(it) }
    }

    companion object CREATOR : Parcelable.Creator<Alert> {
        override fun createFromParcel(parcel: Parcel): Alert {
            return Alert(parcel)
        }

        override fun newArray(size: Int): Array<Alert?> {
            return arrayOfNulls(size)
        }
    }
}
