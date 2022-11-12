package es.ilerna.proyectodam.vehiclegest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.google.android.material.navigation.NavigationBarView
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Añadido
        binding = ActivityMainBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_main)

        setContentView(binding.root)

        binding.bottomNavigation.setOnClickListener(){
            when() {
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

        /* NavigationBarView.OnItemSelectedListener { item ->
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
*/


    }
    private fun replaceFragment(fragment: Fragment) {

        //Ver porque deprecated
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

}