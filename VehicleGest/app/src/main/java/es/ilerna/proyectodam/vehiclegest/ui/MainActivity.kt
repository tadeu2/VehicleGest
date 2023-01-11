package es.ilerna.proyectodam.vehiclegest.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityMainBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.fragmentReplacer
import es.ilerna.proyectodam.vehiclegest.ui.alerts.AlertsFragment
import es.ilerna.proyectodam.vehiclegest.ui.employees.EmployeeFragment
import es.ilerna.proyectodam.vehiclegest.ui.inspections.ItvFragment
import es.ilerna.proyectodam.vehiclegest.ui.inventory.InventoryFragment
import es.ilerna.proyectodam.vehiclegest.ui.login.LoginActivity
import es.ilerna.proyectodam.vehiclegest.ui.services.ServiceFragment
import es.ilerna.proyectodam.vehiclegest.ui.vehicles.VehiclesFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors

/**
 * Actividad principal de la aplicación
 */
class MainActivity : AppCompatActivity() {

    //Variables principales de la actividad
    private lateinit var activityMainBinding: ActivityMainBinding //Binding de la actividad

    //Variables de Firebase
    private lateinit var auth: FirebaseAuth //Autenticación de Firebase
    private lateinit var dbFirestore: FirebaseFirestore //Base de datos de Firebase

    //Variables para crear el contador de alertas
    private var alertCount: Int = 0

    // Referencia a la colección de alertas
    private lateinit var alertCollectionReference: CollectionReference

    // Referencia al badge de alertas
    private lateinit var badgeAlert: BadgeDrawable

    @ExperimentalBadgeUtils
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        //Bindeamos el xml con la actividad y lo inflamos
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        //Establecemos la vista de la actividad
        setContentView(activityMainBinding.root)

        // Inicializa Firebase
        dbFirestore = FirebaseFirestore.getInstance()
        // Inicializa la autenticación de Firebase
        auth = FirebaseAuth.getInstance()

        with(activityMainBinding.appBarMain){

            //Barra de navegación inferior
            setSupportActionBar(topToolbar)

            // Muestra el email del usuario en el subtitulo de la barra de navegación
            topToolbar.subtitle = auth.currentUser?.email.toString()
            topToolbar.setSubtitleTextColor(
                resources.getColor(R.color.ic_launcher_background, null))
            //Activamos el logueo de Firestore para debuggear fallos en el logcat
            FirebaseFirestore . setLoggingEnabled (true)

            //Escuchador del menú superior
            topToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.logout_icon -> {
                        auth.signOut()
                        checkCurrentUser()
                    }
                    R.id.alert_icon -> fragmentReplacer(AlertsFragment(), supportFragmentManager)
                }
                true
            }

            //Carga el fragmento de vehículos como inicial
            fragmentReplacer (VehiclesFragment(), supportFragmentManager
            )
        }


        /**
         * Escuchador del menú inferior, al hacer click en cada uno de los iconos se cargar
         * un fragmento
         **/
        activityMainBinding.bottomBarMain.bottomNavMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.vehicles -> fragmentReplacer(VehiclesFragment(), supportFragmentManager)
                R.id.itv -> fragmentReplacer(ItvFragment(), supportFragmentManager)
                R.id.services -> fragmentReplacer(ServiceFragment(), supportFragmentManager)
                R.id.inventory -> fragmentReplacer(InventoryFragment(), supportFragmentManager)
                R.id.employees -> fragmentReplacer(EmployeeFragment(), supportFragmentManager)
            }
            true
        }

        val inspectionDate = Date()
        val scope = CoroutineScope(Dispatchers.Default)

        scope.launch {
            while (true) {
                val currentDate = Date()
                if (Controller.isInspectionExpired(inspectionDate, currentDate)) {
                    println("La inspección ha caducado")
                } else {
                    println("La inspección todavía no ha caducado")
                }

                // Duerme durante una hora antes de verificar de nuevo
                delay(3_600_000)
            }
        }

    }

    @ExperimentalBadgeUtils
    private fun initializeAlertsBadge() {
            alertCollectionReference = dbFirestore.collection("alert")
            alertCollectionReference.get().addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        alertCount++
                    }
                    // Crea el badge de alertas
                    badgeAlert = BadgeDrawable.create(this)
                    badgeAlert.number = alertCount
                    badgeAlert.isVisible = true
                    // Añade el badge al icono de alertas
                    BadgeUtils.attachBadgeDrawable(
                        badgeAlert,
                        activityMainBinding.appBarMain.topToolbar,
                        R.id.navigation_alerts
                    )
                }
            }
            badgeAlert =
                BadgeDrawable.create(this) //Creamos el badge de alerta para la barra de navegación

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

        initializeAlertsBadge()

        //Pinta la barra superior
        val inflater = menuInflater
        inflater.inflate(R.menu.app_bar_items, menu)

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

