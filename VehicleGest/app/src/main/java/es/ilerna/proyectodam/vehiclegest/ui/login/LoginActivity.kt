package es.ilerna.proyectodam.vehiclegest.ui.login


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityLoginBinding
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity


class LoginActivity :AppCompatActivity(){

    private lateinit var binding: ActivityLoginBinding

    //Declarar la variable auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Creamos el binding y lo inflamos
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos la variable auth de firebase
        auth = Firebase.auth

        //Creamos un listener para el boton que chequea si esta en blanco alguno de los dos campos de texto
        binding.btLogin.setOnClickListener{
            //Con el binding le pasamos los campos de texto de la actividad de autenticacion
            val tie_username = binding.tieUsername.text.toString()
            val tie_password = binding.tiePassword.text.toString()
            //Si no estan en blanco llamamos a la funcion login
            login(tie_username,tie_password)
        }

    }

    public override fun onStart() {
        super.onStart()
        //Comprueba que el usuario actual es el que esta autenticado.
        //val currentUser = auth.currentUser
        //if(currentUser != null){
         // showMain(currentUser)
       // }
    }

    private fun login(username: String, password: String) {
        // Autenticarse con correo electronico y password
        if (username.isNotBlank() || password.isNotBlank())(
                showAlert("Debe rellenar todos los huecos")
        )
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Autenticacion exitosa, ejecuta el metodo de cambio de actividad pasandole el usuario autenticado
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    showMain(user)
                } else {
                    // Si la autenticacion falla manda un mensaje de error
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    showAlert("Autenticación fallida")
                   }
            }
     }

    private fun showAlert(text: String) {
        Toast.makeText(baseContext, text, Toast.LENGTH_SHORT).show()
    }

    //Lanzamos la actividad Main una vez autenticado el usuario pasandole el usuario
    private fun showMain(user: FirebaseUser?) {
        val mainIntent : Intent = Intent(this, MainActivity::class.java).apply {
            putExtra("currentUser", user)
        }
        startActivity(mainIntent)
    }

}