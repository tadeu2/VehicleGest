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
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityRegisterBinding
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity


/**
 * Actividad de registro
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    //Declaramos la variable auth para la autenticación de Firebase auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Creamos el binding con la actividad asociada y la inflamos
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos la variable auth de firebase
        auth = Firebase.auth

        binding.btRegister.setOnClickListener {
            //Con el binding le pasamos los campos de texto de la actividad de autenticacion

            val username: String = binding.tieUsername.text.toString()
            val password: String = binding.tiePassword.text.toString()
            val password2: String = binding.tiePassword2.text.toString()

            if (username.isBlank()) {
                binding.tilUsername.error = getString(R.string.invalid_username)
                Log.w(ContentValues.TAG, "regInWithEmail:Username lenght")
            } else if (password.isBlank() || password.length < 6) {
                binding.tilPassword.error = getString(R.string.invalid_password)
                Log.w(ContentValues.TAG, "regInWithEmail: Invalid password lenght")
            } else if (password != password2) {
                binding.tilPassword.error = getString(R.string.match_password)
                binding.tilPassword2.error = getString(R.string.match_password)
                Log.w(ContentValues.TAG, "regInWithEmail: Invalid password lenght")
            } else {
                binding.tilUsername.error = null
                binding.tilPassword.error = null
                userRegistration(username, password)
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
    private fun userRegistration(username: String, password: String) {
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "regInWithEmail: register success")
                    navigateMain(auth.currentUser)
                } else {
                    Log.w(ContentValues.TAG, "reWithEmail:register failure", task.exception)
                    //Backend.showSnackbar(binding.root, task.exception?.message.toString())
                    MaterialAlertDialogBuilder(this).setTitle(resources.getString(R.string.register_error))
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
        val mainIntent: Intent = Intent(this, MainActivity::class.java).apply {
            putExtra("currentUser", user)
        }
        startActivity(mainIntent)
        finish()
    }

}

