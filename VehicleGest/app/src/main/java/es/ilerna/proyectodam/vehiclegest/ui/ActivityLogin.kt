package es.ilerna.proyectodam.vehiclegest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel:UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.btLogin.setOnClickListener(){
            //login()
        }

    }

    private fun login(){
        val username = binding.tieUsername.text.toString()
        val password = binding.tiePassword.text.toString()

        if (username.isBlank() || password.isBlank()){
            Snackbar.make(this,"Debe ingresar todos los campos",2)
            return
        }
        viewModel.
    }

    fun showSnackBar(){

    }
}