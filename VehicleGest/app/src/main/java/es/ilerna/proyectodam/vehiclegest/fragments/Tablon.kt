package es.ilerna.proyectodam.vehiclegest.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.ilerna.proyectodam.vehiclegest.R

class Tablon : Fragment() {

    companion object {
        fun newInstance() = Tablon()
    }

    private lateinit var viewModel: TablonViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tablon, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TablonViewModel::class.java)
        // TODO: Use the ViewModel
    }

}