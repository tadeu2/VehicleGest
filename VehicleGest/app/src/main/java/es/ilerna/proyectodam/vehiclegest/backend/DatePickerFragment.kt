package es.ilerna.proyectodam.vehiclegest.backend

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

/**
 * Clase encargada de iniciar y mostrar el datePicker para selecionar las fechas
 */

class DatePickerFragment(val listener: (day: Int, month: Int, year: Int) -> Unit) :
    DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(requireContext(), this, year, month, day)
        //dialog.datePicker.maxDate = c.timeInMillis
        return dialog;
    }

    //S llama cuando se seleccione una fecha y nos devuelve los datos seleccionados
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        listener(day, month, year)
    }
}