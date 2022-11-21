package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
/*
class VehiclesViewModel(var vehicleService:IVehicleService=VehicleService()) : ViewModel() {

    //Variable firestore para crear la instancia en el constructor
    private lateinit var firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings
    }


    /**
     * Lanza una corutina para descargar los vehículos de la base de datos?¿
     */
    fun fetchVehicle(){
        viewModelScope.launch {
            var innerVehicles = vehicleService.fetchVehicles()
            vehicles.postValue(innerVehicles)
        }
    }

    /**
     * Guarda los datos del vehículo en la base de datos
     */
    fun save(vehicles:Vehicle){
        // Todo Implementar el salvado en la DB
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is vehicles Fragment"
    }
    val text: LiveData<String> = _text

}

 */