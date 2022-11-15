package es.ilerna.proyectodam.vehiclegest.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityLoginBinding

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    //private val viewModel:UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.btLogin.setOnClickListener {
            login()
        }

    }

    private fun login(){
        val username = binding.tieUsername.text.toString()
        val password = binding.tiePassword.text.toString()
    }

    fun showSnackBar(){

    }
}