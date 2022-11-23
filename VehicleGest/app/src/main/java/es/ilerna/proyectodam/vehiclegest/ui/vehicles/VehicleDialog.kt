package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.backend.Vehiclegest
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityLoginBinding.bind
import es.ilerna.proyectodam.vehiclegest.databinding.DialogVehicleBinding
import java.text.SimpleDateFormat
import java.util.*

class VehicleDialog(val data: Vehicle) : DialogFragment() {

    lateinit var binding: DialogVehicleBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_vehicle, null)
            binding = DialogVehicleBinding.bind(view)
            binding.plateNumber.text = data.plateNumber
            binding.type.text = data.type
            binding.brand.text = data.brand
            binding.model.text = data.model
            //Formatea los timestamp a fecha normal dd/mm/aa
            val simpleDateFormat = SimpleDateFormat(
                getString(R.string.dateFormat), Locale.getDefault()
            )
            val stamp = data.expiryDateITV?.time
            val date = simpleDateFormat.format(Date(stamp!!))
            binding.totalDistance.text = data.totalDistance.toString()

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}