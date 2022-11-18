package es.ilerna.proyectodam.vehiclegest.ui.login


import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseUser
import es.ilerna.proyectodam.vehiclegest.Backend
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityLoginBinding
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity

/**
 * Esta es la actividad principal de la aplicación
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    //Declaramos la variable auth para la autenticación de Firebase auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Creamos el binding con la actividad asociada y la inflamos
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos la variable auth de firebase
        auth = Firebase.auth

        binding.btLogin.setOnClickListener {
            //Con el binding le pasamos los campos de texto de la actividad de autenticacion
            val username: String = binding.tieUsername.text.toString()
            val password: String = binding.tiePassword.text.toString()
            if (username.isBlank()) {
                binding.tilUsername.error =
                    getString(R.string.invalid_username)
                Log.w(ContentValues.TAG, "signInWithEmail: blank username")
            } else if (password.isBlank()) {
                binding.tilPassword.error =
                    getString(R.string.invalid_password)
                Log.w(ContentValues.TAG, "signInWithEmail: blank password")
            } else {
                login(username, password)
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
    private fun login(username: String, password: String) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "signInWithEmail: login success")
                    showMain(auth.currentUser)
                } else {
                    Log.w(ContentValues.TAG, "signInWithEmail:login failure", task.exception)
                }
            }
    }

    /**
     * Lanza la actividad Main una vez autenticado el usuario
     *
     * @param user Usuario de Firebase auth
     * @see Firebase
     */
    private fun showMain(user: FirebaseUser?) {
        val mainIntent: Intent = Intent(this, MainActivity::class.java).apply {
            putExtra("currentUser", user)
        }
        startActivity(mainIntent)
        finish()
    }

}

