package es.ilerna.proyectodam.vehiclegest.ui

import EmployeeFragment
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityMainBinding
import es.ilerna.proyectodam.vehiclegest.ui.alerts.AlertsFragment
import es.ilerna.proyectodam.vehiclegest.ui.inventory.InventoryFragment
import es.ilerna.proyectodam.vehiclegest.ui.inventory.inspections.InspectionsFragment
import es.ilerna.proyectodam.vehiclegest.ui.login.LoginActivity
import es.ilerna.proyectodam.vehiclegest.ui.services.ServicesFragment
import es.ilerna.proyectodam.vehiclegest.ui.vehicles.VehiclesFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var badgeAlert: BadgeDrawable
    private var alertCount = 0
    var alertQuery =  FirebaseFirestore.getInstance().collection("alert")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.topToolbar)

        //Inicializa el icono de alerta
        badgeAlert = BadgeDrawable.create(this)

        // Inicializa el objeto auth de Firebase
        auth = Firebase.auth
        FirebaseFirestore.setLoggingEnabled(true)

        //Carga el fragmento de vehículos como inicial
        replaceFragment(VehiclesFragment())

        //Escuchador del menú superior
        binding.appBarMain.topToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout_icon -> {
                    Firebase.auth.signOut()
                    checkCurrentUser()
                    /*val mainIntent = Intent(this, LoginActivity::class.java)
                    startActivity(mainIntent)
                    finish()*/
                }
                R.id.alert_icon -> replaceFragment(AlertsFragment())
            }
            true
        }

        //Escuchador del menú inferior
        binding.bottomBarMain.bottomNavMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.vehicles -> replaceFragment(VehiclesFragment())
                R.id.itv -> replaceFragment(InspectionsFragment())
                R.id.services -> replaceFragment(ServicesFragment())
                R.id.inventory -> replaceFragment(InventoryFragment())
                R.id.employees -> replaceFragment(EmployeeFragment())
            }
            true
        }

    }

    override fun onStart() {
        super.onStart()
        checkCurrentUser()
    }

    /**
     * Aoscia la barra de herramientas superior de la actividad principal
     */
    @ExperimentalBadgeUtils
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //Crea una subrutina para contar las alartas y actualizar el contador de la campana
        alertQuery.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    alertCount = 0
                    for (document in task.result) {
                        alertCount++
                    }
                }
            }

        badgeAlert.number = alertCount

        //Pinta la barra superior
        var inflater = menuInflater
        inflater.inflate(R.menu.app_bar_items, menu)
        //Crea el contador de alertas y lo asocia al icono de la campana
        BadgeUtils.attachBadgeDrawable(badgeAlert, binding.appBarMain.topToolbar, R.id.alert_icon)

        return super.onCreateOptionsMenu(menu)
    }


    /**
     * Navega entre los fragmentos dentro del layout
     * @param fragment Fragmento que se le pasa para cambiarlo en destino
     */
    fun replaceFragment(fragment: Fragment) {

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

