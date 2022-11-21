package es.ilerna.proyectodam.vehiclegest.data.vehicledata

import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle

/**
 * Clase de datos de vehículo para traernos los datos de la base de datos Firestore
 * La implementamos a base de un
 */
class VehicleDataProvider
{
    companion object{
        val vehicleList = listOf<Vehicle>(
            Vehicle(
                "1234ABC",
                "camion",
                "iveco",
                "asdasd",
                123)
        )
    }
}
