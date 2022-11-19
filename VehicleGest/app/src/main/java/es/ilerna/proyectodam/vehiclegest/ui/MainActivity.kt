package es.ilerna.proyectodam.vehiclegest.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityMainBinding
import es.ilerna.proyectodam.vehiclegest.ui.employees.EmployeesFragment
import es.ilerna.proyectodam.vehiclegest.ui.inventory.inspections.InspectionsFragment
import es.ilerna.proyectodam.vehiclegest.ui.inventory.InventoryFragment
import es.ilerna.proyectodam.vehiclegest.ui.login.LoginActivity
import es.ilerna.proyectodam.vehiclegest.ui.services.ServicesFragment
import es.ilerna.proyectodam.vehiclegest.ui.vehicles.VehiclesFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el objeto auth de Firebase
        auth = Firebase.auth

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(VehiclesFragment())

        //Esuchador del menú inferior
        binding.bottonBarMain.bottomNavMenu.setOnItemSelectedListener {

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
        /*
         //Escuchador del botón de desconexion, vuelve a la actividad login
         val btn: MaterialButton = findViewById<View>(R.id.btLogout) as MaterialButton
         btn.setOnClickListener {
             Log.d(ContentValues.TAG, "signInWithEmail:Usuario deslogueado")
             auth.signOut()
             startActivity(Intent(this, LoginActivity::class.java))
             finish()
         }*/
    }

    /**
     * Navega entre los fragmentos dentro del layout
     * @param fragment Fragmento que se le pasa para cambiarlo en destino
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment)
        fragmentTransaction.commit()
    }

    /**
     * Chequea que el usuario logueado no sea nulo, si lo es vuelve al login
     */
    private fun checkCurrentUser() {
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

}

