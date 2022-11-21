package es.ilerna.proyectodam.vehiclegest.data.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.type.DateTime

data class Vehicle(
    val plateNumber: String,
    val type: String,
    val brand: String,
    val model: String,
   // val expiryDateITV: DateTime,
    val totalDistance: Int
    ){
}