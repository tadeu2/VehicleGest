package es.ilerna.proyectodam.vehiclegest.data.entities

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class ITV : Parcelable {

    var date: Date? = null

    constructor()

    @RequiresApi(Build.VERSION_CODES.Q)
    private constructor(parcel: Parcel) {
        date = Timestamp(parcel.readLong(), 0).toDate()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        date?.time?.let { dest.writeLong(it) }
    }

    companion object CREATOR : Parcelable.Creator<ITV> {
        override fun createFromParcel(parcel: Parcel): ITV {
            return ITV(parcel)
        }

        override fun newArray(size: Int): Array<ITV?> {
            return arrayOfNulls(size)
        }
    }
}
