package es.ilerna.proyectodam.vehiclegest.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import es.ilerna.proyectodam.vehiclegest.Backend.VehicleListener
import es.ilerna.proyectodam.vehiclegest.data.entities.Vehicle
import es.ilerna.proyectodam.vehiclegest.data.vehicledata.VehicleDataProvider
import es.ilerna.proyectodam.vehiclegest.databinding.VehicleCardBinding


/**
 * El adapter se encarga de meter los datos en el recyclerview
 * Implementa a RecyclerView.Adapter
 */
class VehicleRecyclerViewAdapter(
    private val listVehicle: List<Vehicle>,
    private val vehiclelistener: VehicleListener,
    val context: Context
) : RecyclerView.Adapter<VehicleRecyclerViewAdapter.VehicleViewHolder>() {

    /**
     * Llamada para devolver el item(VehicleCard) al viewholder por cada objeto de la lista vehiculos
     *
     */
    @NonNull
    @Override
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): VehicleViewHolder {
        /*
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VehicleCardBinding.inflate(layoutInflater,parent,false)
        return VehicleViewHolder(binding)*/
    }

    /**
     * El recyclerview llama esta función para mostrar los datos en una posición dada
     */
    @Override
    override fun onBindViewHolder(@NonNull vehicleViewHolder: VehicleViewHolder, position: Int) {
        vehicleViewHolder.render(listVehicle[position])
    }

    /**
     * Devuelve el tamaño del listado de vehículos
     */
    @Override
    override fun getItemCount() = listVehicle.size

    /**
     * nested class
     * El holder se encarga de pintar las celdas
     */
    class VehicleViewHolder(
        private val binding: VehicleCardBinding,
        private val listener: VehicleListener,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Rellena cada item de la lista bindeada
         * @param vehicleData Ficha de cada vehículo
         */
        fun render(vehicleData: Vehicle) {
            //TODO Meter un listener para editar los items
           // binding.root.setOnClickListener {
            //   listener.details(data)
            binding.plateNumber.text = vehicleData.plateNumber
            binding.brand.text = vehicleData.brand
            binding.model.text = vehicleData.model
            binding.type.text = vehicleData.type
            //binding.expirydateitv.text  = vehicleData.expiryDateITV
            binding.totaldistance.text = vehicleData.totalDistance.toString()
        }
    }

}