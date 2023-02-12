package es.ilerna.proyectodam.vehiclegest.ui.login


import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityLoginBinding
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity
import kotlinx.coroutines.CoroutineScope


/**
 * Esta es la actividad principal de la aplicación
 */
class LoginActivity : AppCompatActivity() {

    //Enlaza la actividad al xml
    private lateinit var activityLoginBinding: ActivityLoginBinding

    //Declaramos la variable auth para la autenticación de Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    /**
     * Fase de creación de la actividad
     * @param savedInstanceState Bundle de datos
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Creamos el binding con la actividad asociada y la inflamos
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root) //Establece la vista de la actividad
        firebaseAuth = Firebase.auth // Inicializamos la variable auth de firebase

        with(activityLoginBinding) {
            //Crea un escuchador para el botón de login
            btLogin.setOnClickListener {
                //Con el binding le pasamos los campos de texto de la actividad de autenticacion
                val username: String = tieUsername.text.toString()
                val password: String = tiePassword.text.toString()
                if (username.isBlank()) {
                    tilUsername.error = getString(R.string.invalid_username)
                    Log.w(ContentValues.TAG, "Debe introducir un nombre de usuario")
                } else if (password.isBlank() || password.length < 6) {
                    tilPassword.error = getString(R.string.invalid_password)
                    Log.w(ContentValues.TAG, "Longitud incorrecta de la contraseña")
                } else {
                    tilUsername.error = null
                    tilPassword.error = null
                    userAuthentication(username, password) //Llama a la función de autenticación
                }
            }

            //Crea un escuchador para el botón de registro
            btRegister.setOnClickListener {
                navigateRegister()
            }
        }
    }

    /**
     * La función login se encarga de comprobar si los campos están en blanco
     * comprueba tambien si son correctas las credenciales
     *
     * @param username Email o nombre de usuario
     * @param password  Contraseña de usuario
     */
    private fun userAuthentication(username: String, password: String) {
        // TODO: password complexity - Backend.isValidPassword(password)
        firebaseAuth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "signInWithEmail: login success")
                    navigateMain(firebaseAuth.currentUser)
                } else {
                    Log.w(ContentValues.TAG, "signInWithEmail:login failure", task.exception)
                    //Si el login falla muestra un mensaje de error
                    MaterialAlertDialogBuilder(this).setTitle(resources.getString(R.string.authError))
                        .setMessage(task.exception?.message.toString())
                        .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                        }.setIcon(R.drawable.outline_error_24).show()
                }
            }
    }

    /**
     * Lanza la actividad Main una vez autenticado el usuario
     *
     * @param user Usuario de Firebase auth
     * @see Firebase
     */
    private fun navigateMain(user: FirebaseUser?) {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    /**
     * Lanza la actividad register
     *
     */
    private fun navigateRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }
}

