package es.ilerna.proyectodam.vehiclegest.helpers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

/**
 * Clase encargada de iniciar y mostrar el datePicker para selecionar las fechas
 * @param listener Parámetro que contiene el listener del datePicker
 * @param year Parámetro que contiene el año
 * @param month Parámetro que contiene el mes
 * @param day Parámetro que contiene el día
 * @return Devuelve el datePicker con los datos del listener y las fechas
 */

class DatePickerFragment(val listener: (day: Int, month: Int, year: Int) -> Unit) :
    DialogFragment(), DatePickerDialog.OnDateSetListener {
    //Dialogo que muestra el calendario para seleccionar la fecha
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        //dialog.datePicker.maxDate = c.timeInMillis //Para que no se pueda seleccionar una fecha posterior a la actual
        return DatePickerDialog(requireContext(), this, year, month, day);
    }

    /**
     * Función que se ejecuta cuando se selecciona una fecha
     * @param view Parámetro que contiene la vista del datePicker
     * @param year Parámetro que contiene el añ
     * @param month Parámetro que contiene el mes
     * @param day Parámetro que contiene el día
     */
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        //Escuchador que nos devuelve los datos seleccionados
        listener(day, month, year)
    }
}