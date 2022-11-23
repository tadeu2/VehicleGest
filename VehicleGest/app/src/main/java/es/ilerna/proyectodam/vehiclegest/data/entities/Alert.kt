package es.ilerna.proyectodam.vehiclegest.data.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Alert : Parcelable {

    var plateNumber: String? = null
    var type: String? = null
    var brand: String? = null
    var model: String? = null
    //var expiryDateITV: Datetime? = null
    //var totalDistance: Int? = 0

    constructor()

    private constructor(parcel: Parcel) {
        plateNumber = parcel.readString()
        type = parcel.readString()
        brand = parcel.readString()
        model = parcel.readString()
        //plateNumber = parcel.readString()
        //totalDistance = parcel.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(plateNumber)
        dest.writeString(type)
        dest.writeString(brand)
        dest.writeString(model)
        //dest?.writeString(title)
        //dest?.writeInt(totalDistance!!)
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
