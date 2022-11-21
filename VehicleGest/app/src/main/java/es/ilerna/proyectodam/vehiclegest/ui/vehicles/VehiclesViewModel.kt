package es.codigonline.proyecto.smarthome.ui.home

import androidx.lifecycle.*
import es.codigonline.proyecto.smarthome.app.App
import es.codigonline.proyecto.smarthome.database.entities.Favorito
import es.codigonline.proyecto.smarthome.database.entities.Personal
import es.ilerna.proyectodam.vehiclegest.Backend.FragmentType
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VehiclesViewModel : ViewModel() {


    fun vehicles(sectionId: FragmentType.SectionIdEnum): LiveData<List<Vehicle>> {
        return when (sectionId.id) {
            1 -> List<Vehicle>
        }
    }

    fun addFav(dispositivoId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoritoDao.save(Favorito(dispositivoId, App.getUsuario()!!.id))
            }
        }
    }

    fun delFav(dispositivoId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoritoDao.delete(Favorito(dispositivoId, App.getUsuario()!!.id))
            }
        }
    }

    fun addPersonal(dispositivoId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personalDao.save(Personal(dispositivoId, App.getUsuario()!!.id))
            }
        }
    }

    fun delPersonal(dispositivoId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personalDao.delete(Personal(dispositivoId, App.getUsuario()!!.id))
            }
        }
    }
}