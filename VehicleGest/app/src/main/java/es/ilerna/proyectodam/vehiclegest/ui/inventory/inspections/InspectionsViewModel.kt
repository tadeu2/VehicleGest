package es.ilerna.proyectodam.vehiclegest.ui.inventory.inspections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InspectionsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is ITV Fragment"
    }
    val text: LiveData<String> = _text
}