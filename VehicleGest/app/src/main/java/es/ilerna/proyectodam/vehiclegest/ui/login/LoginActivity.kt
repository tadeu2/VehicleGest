package es.ilerna.proyectodam.vehiclegest.ui.login


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityLoginBinding

class LoginActivity :AppCompatActivity(){

    private lateinit var binding: ActivityLoginBinding
    //Declarar la variable auth
    private lateinit var auth: FirebaseAuth
    private var au: String = "sd"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        // Initialize Firebase Auth
        setContentView(binding.root)
        auth = Firebase.auth

        val tie_username = binding.tieUsername.toString()
        val tie_password = binding.tiePassword.toString()
        binding.btLogin.setOnClickListener{
            signIn(tie_username,tie_password)
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    private fun login(username: String, password: String){
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this){}


        val tie_username = binding.tieUsername.toString()
        val tie_password = binding.tiePassword.toString()

            if (tie_username.isBlank() || tie_password.isBlank())(
                //app.showSnackbar()
                    return
            )
    }
    private fun signIn(username: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun reload() {

    }

}