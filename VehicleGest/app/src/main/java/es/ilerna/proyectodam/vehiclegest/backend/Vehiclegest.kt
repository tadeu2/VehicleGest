package es.ilerna.proyectodam.vehiclegest.backend

import android.app.Application
import android.content.Context
import android.view.View
import com.google.android.material.color.DynamicColors
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Matcher
import java.util.regex.Pattern

class Vehiclegest : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        //Aplicar colores dinámicos según el tema
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    companion object {

        private var instance: Vehiclegest? = null

        fun appContext(): Context {
            return instance!!.applicationContext
        }

        fun showSnackbar(view: View, text: String) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .show()
        }

        /**
         *
         *
         *
         *  ^                 # start-of-string
         *  (?=.*[0-9])       # a digit must occur at least once
         *  (?=.*[a-z])       # a lower case letter must occur at least once
         *  (?=.*[A-Z])       # an upper case letter must occur at least once
         *  (?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
         *  (?=\\S+$)         # no whitespace allowed in the entire string
         *  .{4,}             # anything, at least six places though
         *  $                 # end-of-string
         */
        fun isValidPassword(password: CharSequence?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val PASSWORD_PATTERN =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            pattern = Pattern.compile(PASSWORD_PATTERN)
            matcher = pattern.matcher(password)
            return matcher.matches()
        }

    }


}
