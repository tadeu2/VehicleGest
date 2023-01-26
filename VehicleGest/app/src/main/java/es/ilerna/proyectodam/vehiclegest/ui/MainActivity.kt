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

/**
 * Actividad principal de la aplicación
 */
class MainActivity : AppCompatActivity() {

    //Variables principales de la actividad
    private lateinit var activityMainBinding: ActivityMainBinding //Binding de la actividad

    private lateinit var firebaseAuthReference: FirebaseAuth //Autenticación de Firebase
    private lateinit var firestoreDatabaseReference: FirebaseFirestore //Base de datos de Firebase

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
        // Inicializa la instancia de Firestore y firebaseAuth
        firestoreDatabaseReference = FirebaseFirestore.getInstance()
        firebaseAuthReference = FirebaseAuth.getInstance() //Obtenemos la instancia de autenticación de Firebase
        //Activamos el logueo de Firestore para debuggear fallos en el logcat
        FirebaseFirestore.setLoggingEnabled(true)
        //Barra de navegación superior
        setSupportActionBar(activityMainBinding.topBarMain.topToolbar)

        //Configura las opciones de las barras de navegación
        setTopBarSettings()
        setBottomBarSettings()

        //TODO:Subrutina de prueba para la creación de alertas
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

    /**
     * Configura las opciones de la barra superior
     */
    private fun setTopBarSettings() {
        with(activityMainBinding.topBarMain) {

            // Muestra el email del usuario en el subtitulo de la barra de navegación
            topToolbar.subtitle = firebaseAuthReference.currentUser?.email.toString()
            topToolbar.setSubtitleTextColor(
                resources.getColor(R.color.ic_launcher_background, null)
            )
            //Escuchador del menú superior
            topToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.logout_icon -> {
                        firebaseAuthReference.signOut()
                        checkIsLoggedUser()
                    }
                    R.id.alert_icon -> fragmentReplacer(AlertsFragment(), supportFragmentManager)
                }
                true
            }
        }
    }

    /**
     * Configura las opciones del menú inferior
     */
    private fun setBottomBarSettings() {
        //Escuchador del menú inferior, al hacer click en cada uno de los iconos se cargar un fragmento
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
    }

    /**
     * Iniciliaza el badge de alertas en la barra de navegación superior y lo muestra si hay alertas
     */
    @ExperimentalBadgeUtils
    private fun initializeAlertsBadgeCounter() {
        //Variables para crear el contador de alertas
        var alertCount= 0
        // Inicializa la autenticación de Firebase
        val alertCollectionReference: CollectionReference = firestoreDatabaseReference.collection("alert")

        // Si se completa la consulta, se obtiene el número de alertas y se muestra en el badge
        alertCollectionReference.get().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                alertCount = 0
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
                    activityMainBinding.topBarMain.topToolbar,
                    R.id.alert_icon
                )
            }
        }
    }

    /**
     * Cuando la actividad visible para el usuario en el ciclo de vida
     */
    override fun onStart() {
        super.onStart()
        //Chequea si el usuario está logueado
        checkIsLoggedUser()
    }

    /**
     * Asocia la barra de herramientas superior de la actividad principal
     * @param menu Asocia un menú a la actividad
     */
    @ExperimentalBadgeUtils
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Pinta la barra superior
        val inflater = menuInflater
        inflater.inflate(R.menu.app_bar_items, menu)
        initializeAlertsBadgeCounter()
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Chequea que el usuario logueado no sea nulo, si lo es vuelve al login
     */
    private fun checkIsLoggedUser() {
        if (firebaseAuthReference.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}

