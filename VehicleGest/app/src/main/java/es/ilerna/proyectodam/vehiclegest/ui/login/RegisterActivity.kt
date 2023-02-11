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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityRegisterBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.models.Employee
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Actividad de registro
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var activityRegisterBinding: ActivityRegisterBinding

    //Declaramos la variable auth para la autenticación de Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var dbFirestoreReference: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Creamos el binding con la actividad asociada y la inflamos
        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(activityRegisterBinding.root)

//Referencia a la base de datos de Firebase
        dbFirestoreReference = FirebaseFirestore.getInstance().collection("employees")
        // Inicializamos la variable auth de firebase
        firebaseAuth = Firebase.auth

        //Con el binding le pasamos los campos de texto de la actividad de autenticacion
        with(activityRegisterBinding) {
            btRegister.setOnClickListener {
                val username: String = tieUsername.text.toString()
                val password: String = tiePassword.text.toString()
                val passwordRepeat: String = tiePassword2.text.toString()

                if (username.isBlank()) {
                    tilUsername.error = getString(R.string.invalid_username)
                    Log.w(ContentValues.TAG, getString(R.string.invalid_username))
                } else if (password.isBlank() || password.length < 6) {
                    tilPassword.error = getString(R.string.invalid_password)
                    Log.w(ContentValues.TAG, getString(R.string.invalid_password))
                } else if (password != passwordRepeat) {
                    tilPassword.error = getString(R.string.match_password)
                    tilPassword2.error = getString(R.string.match_password)
                    Log.w(ContentValues.TAG, getString(R.string.match_password))
                } else {
                    tilUsername.error = null
                    tilPassword.error = null
                    userRegistration(username, password)
                }
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
        firebaseAuth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "Registro correcto")
                    addUserToDataBase()
                    navigateMain(firebaseAuth.currentUser)
                    Controller.showShortToast("Registro correcto")
                } else {
                    Log.w(ContentValues.TAG, "Registro fallido", task.exception)
                    MaterialAlertDialogBuilder(this).setTitle(resources.getString(R.string.register_error))
                        .setMessage(task.exception?.message.toString())
                        .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                        }.setIcon(R.drawable.outline_error_24).show()
                }
            }
    }

    private fun addUserToDataBase() {
        CoroutineScope(Dispatchers.IO).launch {
            val newUser = newEmployee()
            val currentAuthUser = firebaseAuth.currentUser
            newUser.email = currentAuthUser?.email.toString()
            newUser.birthdate = Controller.stringToDateFormat("12/12/12")
            dbFirestoreReference.document(currentAuthUser?.uid.toString()).set(newUser)
                .addOnSuccessListener {
                    Log.d(
                        ContentValues.TAG,
                        "Usuario escrito}"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error añadiendo Usuario", e)
                }
        }
    }

    /**
     * Devuelve un objeto de tipo empleado con los datos del formulario
     * @return Objeto de tipo empleado
     */
    private fun newEmployee(): Employee {
        activityRegisterBinding.apply {
            return Employee(
                "",
                "",
                "",
                "",
                "",
                "",
                null,
                "",
                false
            )
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

