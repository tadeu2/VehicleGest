package es.ilerna.proyectodam.vehiclegest.ui.itv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ITVViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is ITV Fragment"
    }
    val text: LiveData<String> = _text
}