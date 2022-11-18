package es.ilerna.proyectodam.vehiclegest

import android.app.Application
import android.view.View
import com.google.android.material.snackbar.Snackbar

class Backend: Application() {

        init {
            instancia = this

        }
    companion object {
        private lateinit var instancia: Backend

        fun showSnackbar(view: View, text: String) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .show()
        }
    }

}