package es.ilerna.proyectodam.vehiclegest.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityMainBinding
import es.ilerna.proyectodam.vehiclegest.helpers.DataHelper.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.ui.alerts.AlertsFragment
import es.ilerna.proyectodam.vehiclegest.ui.employees.EmployeeFragment
import es.ilerna.proyectodam.vehiclegest.ui.inspections.ItvFragment
import es.ilerna.proyectodam.vehiclegest.ui.inventory.InventoryFragment
import es.ilerna.proyectodam.vehiclegest.ui.login.LoginActivity
import es.ilerna.proyectodam.vehiclegest.ui.services.ServiceFragment
import es.ilerna.proyectodam.vehiclegest.ui.vehicles.VehiclesFragment

/**
 * Actividad principal de la aplicación
 */
class MainActivity : AppCompatActivity() {

    //Variables principales de la actividad
    private lateinit var binding: ActivityMainBinding

    //Variables de Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var dbFirestore: FirebaseFirestore

    //Variables para crear el contador de alertas
    private var alertCount: Int = 0
    private lateinit var alertQuery: CollectionReference
    private lateinit var badgeAlert: BadgeDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Bindeamos el xml con la actividad y lo inflamos
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.topToolbar)

        // Inicializa Firebase
        dbFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        binding.appBarMain.topToolbar.subtitle = auth.currentUser?.email.toString()

        //Activamos el logueo de Firestore para debuggear fallos en el logcat
        FirebaseFirestore.setLoggingEnabled(true)

        //Inicializa el icono de alerta
        alertQuery = dbFirestore.collection("alert")
        badgeAlert = BadgeDrawable.create(this)

        //Carga el fragmento de vehículos como inicial
        fragmentReplacer(VehiclesFragment(), supportFragmentManager)

        //Escuchador del menú superior
        binding.appBarMain.topToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout_icon -> {
                    auth.signOut()
                    checkCurrentUser()
                }
                R.id.alert_icon -> fragmentReplacer(AlertsFragment(), supportFragmentManager)
            }
            true
        }

        /**
         * Escuchador del menú inferior, al hacer click en cada uno de los iconos se cargar
         * un fragmento
         **/
        binding.bottomBarMain.bottomNavMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.vehicles -> fragmentReplacer(VehiclesFragment(), supportFragmentManager)
                R.id.itv -> fragmentReplacer(ItvFragment(), supportFragmentManager)
                R.id.services -> fragmentReplacer(ServiceFragment(), supportFragmentManager)
                R.id.inventory -> fragmentReplacer(InventoryFragment(), supportFragmentManager)
                R.id.employees -> fragmentReplacer(EmployeeFragment(), supportFragmentManager)
            }
            true
        }

    }

    /**
     * Cuando la actividad visible para el usuario en el ciclo de vida
     */
    override fun onStart() {
        super.onStart()
        //Chequea si el usuario está logueado
        checkCurrentUser()
    }

    /**
     * Asocia la barra de herramientas superior de la actividad principal
     * @param menu Asocia un menú a la actividad
     */
    @ExperimentalBadgeUtils
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //Crea una subrutina para contar los documentos de alerta y actualiza el contador de la campana
        alertQuery.get().addOnCompleteListener { task ->
            //Cada la tarea se completa se reinicia el contador
            if (task.isSuccessful) {
                alertCount = 0
                //Se ejecuta un bucle que cuenta cada documento de la tarea
                for (document in task.result) {
                    alertCount++
                }
            }
        }
        //Asigna al contador de alertas la variable creada en la tarea
        badgeAlert.number = alertCount

        //Pinta la barra superior
        val inflater = menuInflater
        inflater.inflate(R.menu.app_bar_items, menu)
        //Crea el contador de alertas y lo asocia al icono de la campana
        BadgeUtils.attachBadgeDrawable(badgeAlert, binding.appBarMain.topToolbar, R.id.alert_icon)

        return super.onCreateOptionsMenu(menu)
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

