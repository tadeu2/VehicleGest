package es.ilerna.proyectodam.vehiclegest.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityMainBinding
import es.ilerna.proyectodam.vehiclegest.ui.employees.EmployeesFragment
import es.ilerna.proyectodam.vehiclegest.ui.inspections.InspectionsFragment
import es.ilerna.proyectodam.vehiclegest.ui.inventory.InventoryFragment
import es.ilerna.proyectodam.vehiclegest.ui.login.LoginActivity
import es.ilerna.proyectodam.vehiclegest.ui.services.ServicesFragment
import es.ilerna.proyectodam.vehiclegest.ui.vehicles.VehiclesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(VehiclesFragment())

        binding.bottomNavMenu.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.vehicles -> replaceFragment(VehiclesFragment())
                R.id.itv -> replaceFragment(InspectionsFragment())
                R.id.services -> replaceFragment(ServicesFragment())
                R.id.inventory -> replaceFragment(InventoryFragment())
                R.id.employees -> replaceFragment(EmployeesFragment())
                else -> {

                }
            }
            true
        }

    }

    override fun onStart() {
        super.onStart()
        //Chequea que el usuario logueado no sea nulo, si lo es vuelve al login
        checkCurrentUser()
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment)
        fragmentTransaction.commit()

    }

        private fun checkCurrentUser() {
            // [START check_current_user]
            val user = Firebase.auth.currentUser
            if (user != null) {

            } else {
                showLogin()
            }
        }

    private fun showLogin() {
        val loginIntent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

}

