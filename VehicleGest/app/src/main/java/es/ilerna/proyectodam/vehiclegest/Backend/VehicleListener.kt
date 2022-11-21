package es.ilerna.proyectodam.vehiclegest.Backend

import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle

interface VehicleListener {
    fun addVehicle(id: Long)
    fun delVehicle(id: Long)
    fun addITV(id: Long)
    fun delITV(id: Long)
    fun delPersonal(id: Long)
    fun details(dispositivoCompleto: Vehicle)
    fun edit(dispositivoCompleto: Vehicle)
}