package es.ilerna.proyectodam.vehiclegest.ui.vehicles

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.R
import es.ilerna.proyectodam.vehiclegest.databinding.DetailVehicleBinding
import es.ilerna.proyectodam.vehiclegest.helpers.Controller.Companion.stringToDateFormat
import es.ilerna.proyectodam.vehiclegest.helpers.DatePickerFragment
import es.ilerna.proyectodam.vehiclegest.interfaces.DetailModelFragment
import es.ilerna.proyectodam.vehiclegest.models.Vehicle

/**
 * Abre una ventana diálogo con los detalles del vehículo
 */
class VehicleAdder : DetailModelFragment() {

    //Variable para enlazar el achivo de código con el XML de interfaz
    private var addVehicleBinding: DetailVehicleBinding? = null
    private val getAddVehicleBinding get() = addVehicleBinding!!

    //Variable para la base de datos
    private lateinit var vehicleCollectionReference: CollectionReference

    /**
     * Fase de creación de la vista del fragmento
     * @param inflater  Inflador de la vista
     * @param container Contenedor de la vista
     * @param savedInstanceState Instancia guardada
     * @return Vista del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        try {
            //Inicializa la base de datos
            dbFirestoreReference = FirebaseFirestore.getInstance().collection("vehicle")

            //Enlaza al XML del formulario y lo infla
            addVehicleBinding = DetailVehicleBinding.inflate(inflater, container, false)

            makeFormEditable() //Habilita los campos para su edición

            with(getAddVehicleBinding.bar) {
                btsave.visibility = View.VISIBLE
                btedit.visibility = View.GONE
                btdelete.visibility = View.GONE
                setListeners(
                    null,
                    parentFragmentManager,
                    VehiclesFragment(),
                    btclose,
                    btdelete,
                    btsave,
                    btedit
                )
            }

        } catch (exception: Exception) {
            Log.e(TAG, exception.message.toString(), exception)
        }
        //Llama a la función que rellena los datos en el formulario
        return getAddVehicleBinding.root
    }

    /**
     * Metodo que rellena el formulario con los datos de la entidad
     */
    override fun bindDataToForm() {
        TODO("Not yet implemented")
    }

    /**
     * Metodo que rellena la entidad con los datos del formulario
     */
    override fun fillDataFromForm(): Any {
        getAddVehicleBinding.apply {
            val inputString = totalDistance.text.toString()
            val inputNumber = if(inputString.isNullOrEmpty()) 0 else inputString.toInt()
            return Vehicle(
                    plateNumber.text.toString(),
                    type.text.toString(),
                    brand.text.toString(),
                    model.text.toString(),
                    stringToDateFormat(expiringItvDate.text.toString()),
                    inputNumber,
                    checkItvPassed.isChecked,
                    vehicleDescription.text.toString(),
                    urlphoto.text.toString()
                )
        }
    }

    /**
     *  Hace el formulario editable
     */
    override fun makeFormEditable() {
        getAddVehicleBinding.apply {
            plateNumber.isEnabled = true
            plateNumber.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            type.isEnabled = true
            type.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            brand.isEnabled = true
            brand.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            model.isEnabled = true
            model.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            expiringItvDate.isEnabled = true
            expiringItvDate.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            totalDistance.isEnabled = true
            totalDistance.setTextColor(
                resources.getColor(
                    R.color.md_theme_dark_errorContainer,
                    null
                )
            )
            checkItvPassed.isEnabled = true
            checkItvPassed.setTextColor(
                resources.getColor(
                    R.color.md_theme_dark_errorContainer,
                    null
                )
            )
            vehicleDescription.isEnabled = true
            vehicleDescription.setTextColor(
                resources.getColor(
                    R.color.md_theme_dark_errorContainer,
                    null
                )
            )
            urlphoto.isEnabled = true
            urlphoto.setTextColor(resources.getColor(R.color.md_theme_dark_errorContainer, null))
            //Escuchador del botón de fecha
            expiringItvDate.setOnClickListener {
                //Abre el selector de fecha
                DatePickerFragment { day, month, year ->
                    //Muestra la fecha en el campo de texto
                    expiringItvDate.setText(String.format("$day/$month/$year"))
                }.show(parentFragmentManager, "datePicker")
            }
        }
    }

}