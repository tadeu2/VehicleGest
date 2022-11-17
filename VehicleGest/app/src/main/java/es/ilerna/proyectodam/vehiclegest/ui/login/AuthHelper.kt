package es.ilerna.proyectodam.vehiclegest.ui.login

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity

class AuthHelper {
/*
    fun login(username: String, password: String) {
        // Autenticarse con correo electronico y password
        if (username.isBlank() || password.isBlank()) (showAlert("Debe rellenar todos los huecos"))
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Autenticacion exitosa, ejecuta el metodo de cambio de actividad pasandole el usuario autenticado
                Log.d(ContentValues.TAG, "signInWithEmail:success")
                val user = auth.currentUser
                showMain(user)
            } else {
                // Si la autenticacion falla manda un mensaje de error
                Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                showAlert("Autenticación fallida")
            }
        }
    }

    fun logout() {
        val user = Firebase.auth.currentUser

    }

    //Lanzamos la actividad Main una vez autenticado el usuario pasandole el usuario
    private fun navigateTo(user: FirebaseUser?) {
        val mainIntent : Intent = Intent(   , MainActivity::class.java).apply {
            putExtra("currentUser", user)
        }
        startActivity(mainIntent)
    }*/
}