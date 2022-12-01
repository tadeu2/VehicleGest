package es.ilerna.proyectodam.vehiclegest.data.entities

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class Item : Parcelable {

    var plateNumber: String? = null
    var name: String? = null
    var description: String? = null
    var photoURL: String? = null

    constructor()

    @RequiresApi(Build.VERSION_CODES.Q)
    private constructor(parcel: Parcel) {
        plateNumber = parcel.readString()
        name = parcel.readString()
        description = parcel.readString()
        photoURL = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(plateNumber)
        dest.writeString(name)
        dest.writeString(description)
        dest.writeString(photoURL)
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}
