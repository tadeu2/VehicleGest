package es.ilerna.proyectodam.vehiclegest.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.ilerna.proyectodam.vehiclegest.databinding.ActivityMainBinding
import es.ilerna.proyectodam.vehiclegest.databinding.VehicleCardBinding
import java.util.concurrent.RecursiveAction

class VehicleRecyclerAdapter(var listVehicle:List<Vehicle>, private val listener: DeviceListener,val context: Context
): RecyclerView.Adapter<VehicleRecyclerAdapter.ViewHolder>() {

    /**
     * Referencia a los tipos de vistas que usamos.
     */
    class ViewHolder private constructor(
        private val binding: VehicleCardBinding,
        private val listener: VehicleListener,
    ): RecyclerView.ViewHolder(binding.root){
        fun rellenarDatos(data: DispositivoCompleto) {
            binding.root.setOnClickListener {
                listener.details(data)
            }
            binding.nombre.text = data.dispositivo.nombre
            binding.precio.text = context.getString(R.string.show_precio, data.dispositivo.precio)
            binding.marca.text = data.marca
            binding.tipo.text = data.tipo
            binding.URL.text = context.getString(R.string.externo)
            binding.URL.setTextColor(Color.BLUE)
            binding.URL.setOnClickListener {
                listener.open(data.dispositivo.url)
            }
            binding.edit.setOnClickListener {
                listener.edit(data)
            }
            if (data.favorito) {
                binding.like.setIconTintResource(R.color.red)

            } else {
                binding.like.setIconTintResource(R.color.md_theme_light_outline)
            }
            if (data.personal) {
                binding.personal.setIconTintResource(R.color.green)
            } else {
                binding.personal.setIconTintResource(R.color.md_theme_light_outline)
            }
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide
                .with(context)
                .load(data.dispositivo.imagen)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(binding.imagen)

            binding.like.setOnClickListener {
                if (data.favorito) {
                    listener.delFavorito(data.dispositivo.id)
                    binding.like.setIconTintResource(R.color.md_theme_light_outline)
                } else {
                    binding.like.setIconTintResource(R.color.red)
                    listener.addFavorito(data.dispositivo.id)
                }
            }
            binding.personal.setOnClickListener {
                if (data.personal) {
                    listener.delPersonal(data.dispositivo.id)
                    binding.personal.setIconTintResource(R.color.md_theme_light_outline)
                } else {
                    listener.addPersonal(data.dispositivo.id)
                    binding.personal.setIconTintResource(R.color.green)
                }
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.newInstance(parent, listener, context)

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.rellenarDatos(list[position])

    override fun getItemCount() = list.size

    companion object {
        fun newInstance(
            parent: ViewGroup, listener: DeviceListener, context: Context
        ): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DeviceBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding, listener, context)
        }
    }
}
interface VehicleListener {
    fun open(url: String)
    fun addFavorito(id: Long)
    fun delFavorito(id: Long)
    fun addPersonal(id: Long)
    fun delPersonal(id: Long)
    fun details(VehicleCp: DispositivoCompleto)
    fun edit(VehicleCp: DispositivoCompleto)
}