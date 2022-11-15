package es.ilerna.proyectodam.vehiclegest.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_vehicles,R.id.navigation_services,
                R.id.navigation_itv,R.id.navigation_employees,R.id.navigation_inventory)
            )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.vehiculos -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.itv -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.servicios -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.inventario -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.personal -> {
                    // Respond to navigation item 2 click
                    true
                }
                else -> false
            }
        }
    }
}

