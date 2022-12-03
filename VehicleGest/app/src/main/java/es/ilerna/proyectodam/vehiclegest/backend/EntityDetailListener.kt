package es.ilerna.proyectodam.vehiclegest.backend

import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle

/**
 * Interfaz para implementar como se comportará al hacer click a una ficha o al botón de añadir
 */
interface EntityAdapterListener {
    fun onEditButton(vehicle: Vehicle?)
    fun onDeleteButton(vehicle: Vehicle?)
}