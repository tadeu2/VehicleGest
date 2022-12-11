package es.ilerna.proyectodam.vehiclegest.backend

import java.util.*

/**
 * Interfaz para crear escuchadores para las diferentes entidades de la base de datos Firestore
 */
interface IEntityListener {
    fun addEntity(id: String)
    fun details(entity: Objects)
    fun edit(vehicle: Objects)
    fun delEntity(id: String)
}