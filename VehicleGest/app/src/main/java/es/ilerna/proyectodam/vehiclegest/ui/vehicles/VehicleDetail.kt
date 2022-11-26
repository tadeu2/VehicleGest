package es.ilerna.proyectodam.vehiclegest.ui.vehicles


import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.databinding.DialogVehicleBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class VehicleDetail(val data: Vehicle) : AppCompatActivity() {

    lateinit var binding: DialogVehicleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_vehicle, null)
            builder.setView(view)
            val dialog = builder.create()

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
            binding.btclose.setOnClickListener(View.OnClickListener { dialog?.dismiss() })

            dialog
        } ?: throw IllegalStateException("Dialog cant be null")
    }
}