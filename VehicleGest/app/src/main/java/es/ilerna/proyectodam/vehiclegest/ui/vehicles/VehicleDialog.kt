package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.DialogVehicleBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class VehicleDialog(val data: Vehicle) : DialogFragment() {

    lateinit var binding: DialogVehicleBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_vehicle, null)
            binding = DialogVehicleBinding.bind(view)
            binding.plateNumber.text = data.plateNumber
            binding.type.text = data.type
            binding.brand.text = data.brand
            binding.model.text = data.model
            binding.vehicleDescription.text = data.description
            binding.checkLicensed.isChecked = data.licensed == true
            //Formatea los timestamp a fecha normal dd/mm/aa
            val simpleDateFormat = SimpleDateFormat(
                getString(R.string.dateFormat), Locale.getDefault()
            )
            val stamp = data.expiryDateITV?.time
            val date = simpleDateFormat.format(Date(stamp!!))
            binding.totalDistance.text = data.totalDistance.toString()

            builder.setNegativeButton("Close") { dialog, which ->
                    dialog.cancel()
                }

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Dialog cant be null")
    }
}